package org.hang.live.stream.room.provider.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.hang.live.common.interfaces.enums.CommonStatusEnum;
import org.hang.live.common.interfaces.topic.GiftProviderTopicNames;
import org.hang.live.common.redis.configuration.key.StreamRoomProviderCacheKeyBuilder;
import org.hang.live.common.interfaces.dto.PageWrapper;
import org.hang.live.common.interfaces.utils.ConvertBeanUtils;
import org.hang.live.im.core.server.interfaces.constants.AppIdEnum;
import org.hang.live.im.core.server.interfaces.dto.ImOfflineDTO;
import org.hang.live.im.core.server.interfaces.dto.ImOnlineDTO;
import org.hang.live.im.core.server.interfaces.dto.ImMsgBody;
import org.hang.live.im.server.router.interfaces.constants.ImMsgBizCodeEnum;
import org.hang.live.im.server.router.interfaces.rpc.ImRouterRPC;
import org.hang.live.stream.room.interfaces.constants.LiveStreamRoomTypeEnum;
import org.hang.live.stream.room.interfaces.dto.LivePkStreamRoomRespDTO;
import org.hang.live.stream.room.interfaces.dto.LiveStreamRoomReqDTO;
import org.hang.live.stream.room.interfaces.dto.LiveStreamRoomRespDTO;
import org.hang.live.stream.room.provider.dao.mapper.LiveStreamRoomMapper;
import org.hang.live.stream.room.provider.dao.mapper.LiveStreamRoomRecordMapper;
import org.hang.live.stream.room.provider.dao.po.LiveStreamRoomPO;
import org.hang.live.stream.room.provider.service.ILiveStreamRoomService;
import org.hang.live.stream.room.provider.service.ILiveStreamRoomCloseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author hang
 * @Date: Created in 21:24 2024/8/13
 * @Description
 */
@Service
public class LiveStreamRoomServiceImpl implements ILiveStreamRoomService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LiveStreamRoomServiceImpl.class);

    @Resource
    private LiveStreamRoomMapper liveStreamRoomMapper;
    @Resource
    private MQProducer mqProducer;
    @Resource
    private LiveStreamRoomRecordMapper liveStreamRoomRecordMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private StreamRoomProviderCacheKeyBuilder cacheKeyBuilder;
    @Resource
    private ILiveStreamRoomCloseService livingRoomTxService;
    @DubboReference
    private ImRouterRPC imRouterRpc;

    //Get all online users connected in a room
    @Override
    public List<Long> queryUserIdByRoomId(LiveStreamRoomReqDTO liveStreamRoomReqDTO) {
        Integer roomId = liveStreamRoomReqDTO.getRoomId();
        Integer appId = liveStreamRoomReqDTO.getAppId();
        String cacheKey = cacheKeyBuilder.buildLiveStreamRoomUserSet(roomId, appId);
        //Here we get 100 ids each, 0-99,100-199,200-299
        //Due to distributed situation, it should not scan all message at once to avoid Redis performance degrade.
        Cursor<Object> cursor = redisTemplate.opsForSet().scan(cacheKey, ScanOptions.scanOptions().match("*").count(100).build());
        List<Long> userIdList = new ArrayList<>();
        while (cursor.hasNext()) {
            Integer userId = (Integer) cursor.next();
            userIdList.add(Long.valueOf(userId));
        }
        return userIdList;
    }

    @Override
    public void handleUserOfflineConnection(ImOfflineDTO imOfflineDTO) {
        LOGGER.info("offline handler,imOfflineDTO is {}", imOfflineDTO);
        Long userId = imOfflineDTO.getUserId();
        Integer roomId = imOfflineDTO.getRoomId();
        Integer appId = imOfflineDTO.getAppId();
        String cacheKey = cacheKeyBuilder.buildLiveStreamRoomUserSet(roomId, appId);
        redisTemplate.opsForSet().remove(cacheKey, userId);
        //When user get offline
        LiveStreamRoomReqDTO roomReqDTO = new LiveStreamRoomReqDTO();
        roomReqDTO.setRoomId(imOfflineDTO.getRoomId());
        roomReqDTO.setPkObjId(imOfflineDTO.getUserId());
        roomReqDTO.setAnchorId(imOfflineDTO.getUserId());
        this.leaveOnlinePK(roomReqDTO);
        //When broadcaster logout im server, close the live stream room as well.
        livingRoomTxService.closeLiveStreamRoom(roomReqDTO);
    }

    @Override
    public void handleUserOnlineConnection(ImOnlineDTO imOnlineDTO) {
        LOGGER.info("online handler,imOnlineDTO is {}", imOnlineDTO);
        Long userId = imOnlineDTO.getUserId();
        Integer roomId = imOnlineDTO.getRoomId();
        Integer appId = imOnlineDTO.getAppId();
        String cacheKey = cacheKeyBuilder.buildLiveStreamRoomUserSet(roomId, appId);
        //set connected user in room user list
        redisTemplate.opsForSet().add(cacheKey, userId);
        redisTemplate.expire(cacheKey, 12, TimeUnit.HOURS);
    }

    @Override
    public List<LiveStreamRoomRespDTO> listAllLiveStreamRoomsFromDB(Integer type) {
        //Get at most 1000 rooms in the front page.
        LambdaQueryWrapper<LiveStreamRoomPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LiveStreamRoomPO::getStatus, CommonStatusEnum.VALID_STATUS.getCode());
        queryWrapper.eq(LiveStreamRoomPO::getType, type);
        //based on id desc order.
        queryWrapper.orderByDesc(LiveStreamRoomPO::getId);
        queryWrapper.last("limit 1000");
        return ConvertBeanUtils.convertList(liveStreamRoomMapper.selectList(queryWrapper), LiveStreamRoomRespDTO.class);
    }

    @Override
    public PageWrapper<LiveStreamRoomRespDTO> list(LiveStreamRoomReqDTO liveStreamRoomReqDTO) {
        //Get a page of rooms (20 to 30) in the front page.
        //The room is already cached and update every 1 second. So we can directly check Redis.
        String cacheKey = cacheKeyBuilder.buildLiveStreamRoomList(liveStreamRoomReqDTO.getType());
        int page = liveStreamRoomReqDTO.getPage();
        int pageSize = liveStreamRoomReqDTO.getPageSize();
        long total = redisTemplate.opsForList().size(cacheKey);
        List<Object> resultList = redisTemplate.opsForList().range(cacheKey, (page - 1) * pageSize, (page * pageSize));
        PageWrapper<LiveStreamRoomRespDTO> pageWrapper = new PageWrapper<>();
        if (CollectionUtils.isEmpty(resultList)) {
            pageWrapper.setList(Collections.emptyList());
            pageWrapper.setHasNext(false);
            return pageWrapper;
        } else {
            List<LiveStreamRoomRespDTO> LiveStreamRoomRespDTOS = ConvertBeanUtils.convertList(resultList, LiveStreamRoomRespDTO.class);
            pageWrapper.setList(LiveStreamRoomRespDTOS);
            pageWrapper.setHasNext(page * pageSize < total);
            return pageWrapper;
        }
    }

    @Override
    public LiveStreamRoomRespDTO queryByRoomId(Integer roomId) {
        String cacheKey = cacheKeyBuilder.buildLiveStreamRoomObj(roomId);
        LiveStreamRoomRespDTO queryResult = (LiveStreamRoomRespDTO) redisTemplate.opsForValue().get(cacheKey);
        if (queryResult != null) {
            //Empty value cached.
            if (queryResult.getId() == null) {
                return null;
            }
            return queryResult;
        }
        LambdaQueryWrapper<LiveStreamRoomPO> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(LiveStreamRoomPO::getId, roomId);
        queryWrapper.eq(LiveStreamRoomPO::getStatus, CommonStatusEnum.VALID_STATUS.getCode());
        queryWrapper.last("limit 1");
        queryResult = ConvertBeanUtils.convert(liveStreamRoomMapper.selectOne(queryWrapper), LiveStreamRoomRespDTO.class);
        if (queryResult == null) {
            //Empty value cached.
            redisTemplate.opsForValue().set(cacheKey, new LiveStreamRoomRespDTO(), 1, TimeUnit.MINUTES);
            return null;
        }
        if (LiveStreamRoomTypeEnum.PK_LIVE_STREAM_ROOM.getCode().equals(queryResult.getType())) {
            queryResult.setPkObjId(this.queryOnlinePkUserId(roomId));
        }
        redisTemplate.opsForValue().set(cacheKey, queryResult, 30, TimeUnit.MINUTES);
        return queryResult;
    }

    @Override
    public Integer startLiveStreamRoom(LiveStreamRoomReqDTO liveStreamRoomReqDTO) {
        LiveStreamRoomPO LiveStreamRoomPO = ConvertBeanUtils.convert(liveStreamRoomReqDTO, LiveStreamRoomPO.class);
        LiveStreamRoomPO.setStatus(CommonStatusEnum.VALID_STATUS.getCode());
        LiveStreamRoomPO.setStartTime(new Date());
        liveStreamRoomMapper.insert(LiveStreamRoomPO);
        String cacheKey = cacheKeyBuilder.buildLiveStreamRoomObj(LiveStreamRoomPO.getId());
        //When the room is started
        redisTemplate.delete(cacheKey);

        Message message = new Message();
        message.setTopic(GiftProviderTopicNames.PREPARE_STOCK);
        message.setBody(String.valueOf(liveStreamRoomReqDTO.getAnchorId()).getBytes());
        SendResult sendResult = null;
        try {
            sendResult = mqProducer.send(message);
            LOGGER.info("[StartLiveStreamRoom] send message to anchor to prepare stock is {}", sendResult);
        } catch (Exception e) {}

        return LiveStreamRoomPO.getId();
    }


    @Override
    public Long queryOnlinePkUserId(Integer roomId) {
        String cacheKey = cacheKeyBuilder.buildLiveStreamRoomOnlinePk(roomId);
        Object userId = redisTemplate.opsForValue().get(cacheKey);
        return userId != null ? Long.valueOf((int) userId) : null;
    }

    @Override
    public LivePkStreamRoomRespDTO joinOnlinePK(LiveStreamRoomReqDTO liveStreamRoomReqDTO) {
        LiveStreamRoomRespDTO currentLivingRoom = this.queryByRoomId(liveStreamRoomReqDTO.getRoomId());
        LivePkStreamRoomRespDTO respDTO = new LivePkStreamRoomRespDTO();
        respDTO.setOnlineStatus(false);
        if (currentLivingRoom.getAnchorId().equals(liveStreamRoomReqDTO.getPkObjId())) {
            respDTO.setMsg("You are the anchor of this room, so yourself cannot enter PK.");
            return respDTO;
        }
        String cacheKey = cacheKeyBuilder.buildLiveStreamRoomOnlinePk(liveStreamRoomReqDTO.getRoomId());
        boolean tryOnline = redisTemplate.opsForValue().setIfAbsent(cacheKey, liveStreamRoomReqDTO.getPkObjId(), 30, TimeUnit.HOURS);
        if (tryOnline) {
            List<Long> userIdList = this.queryUserIdByRoomId(liveStreamRoomReqDTO);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("pkObjId", liveStreamRoomReqDTO.getPkObjId());
            jsonObject.put("pkObjAvatar", "https://picdm.sunbangyan.cn/2023/08/29/w2qq1k.jpeg");
            batchSendImMsg(userIdList, ImMsgBizCodeEnum.LIVING_ROOM_PK_ONLINE.getCode(), jsonObject);
            respDTO.setMsg("pk connection success");
            respDTO.setOnlineStatus(true);
        } else {
            respDTO.setMsg("There is someone else in the pk. Try it later.");
        }
        return respDTO;
    }

    @Override
    public boolean leaveOnlinePK(LiveStreamRoomReqDTO liveStreamRoomReqDTO) {
        Integer roomId = liveStreamRoomReqDTO.getRoomId();
        Long pkUserId = this.queryOnlinePkUserId(roomId);
        // delete if the one who is offline is user joined in PK.
        if (!liveStreamRoomReqDTO.getPkObjId().equals(pkUserId)) {
            System.out.println("Delete Failed");
            return false;
        }
        System.out.println("Delete Success");
        String cacheKey = cacheKeyBuilder.buildLiveStreamRoomOnlinePk(roomId);
        //Delete PK progress
        redisTemplate.delete("live-gift-provider:living_pk_key:" + roomId);
        //delete PK room connected user
        return Boolean.TRUE.equals(redisTemplate.delete(cacheKey));
    }

    private void batchSendImMsg(List<Long> userIdList, int bizCode, JSONObject jsonObject) {
        List<ImMsgBody> imMsgBodies = userIdList.stream().map(userId -> {
            ImMsgBody imMsgBody = new ImMsgBody();
            imMsgBody.setAppId(AppIdEnum.LIVE_BIZ.getCode());
            imMsgBody.setBizCode(bizCode);
            imMsgBody.setUserId(userId);
            imMsgBody.setData(jsonObject.toJSONString());
            return imMsgBody;
        }).collect(Collectors.toList());
        imRouterRpc.batchSendMsg(imMsgBodies);
    }

    @Override
    public LiveStreamRoomRespDTO queryByAnchorId(Long anchorId){
        LambdaQueryWrapper<LiveStreamRoomPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LiveStreamRoomPO::getAnchorId, anchorId);
        queryWrapper.eq(LiveStreamRoomPO::getStatus, CommonStatusEnum.VALID_STATUS.getCode());
        queryWrapper.last("limit 1");
        return ConvertBeanUtils.convert(liveStreamRoomMapper.selectOne(queryWrapper), LiveStreamRoomRespDTO.class);
    }

}
