package org.hang.live.living.provider.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.hang.live.framework.redis.starter.key.LivingProviderCacheKeyBuilder;
import org.hang.live.common.interfaces.dto.PageWrapper;
import org.hang.live.common.interfaces.enums.CommonStatusEum;
import org.hang.live.common.interfaces.utils.ConvertBeanUtils;
import org.hang.live.im.constants.AppIdEnum;
import org.hang.live.im.core.server.interfaces.dto.ImOfflineDTO;
import org.hang.live.im.core.server.interfaces.dto.ImOnlineDTO;
import org.hang.live.im.dto.ImMsgBody;
import org.hang.live.im.router.interfaces.constants.ImMsgBizCodeEnum;
import org.hang.live.im.router.interfaces.rpc.ImRouterRpc;
import org.hang.live.living.interfaces.constants.LivingRoomTypeEnum;
import org.hang.live.living.interfaces.dto.LivingPkRespDTO;
import org.hang.live.living.interfaces.dto.LivingRoomReqDTO;
import org.hang.live.living.interfaces.dto.LivingRoomRespDTO;
import org.hang.live.living.provider.dao.mapper.LivingRoomMapper;
import org.hang.live.living.provider.dao.mapper.LivingRoomRecordMapper;
import org.hang.live.living.provider.dao.po.LivingRoomPO;
import org.hang.live.living.provider.service.ILivingRoomService;
import org.hang.live.living.provider.service.ILivingRoomTxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
public class LivingRoomServiceImpl implements ILivingRoomService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LivingRoomServiceImpl.class);

    @Resource
    private LivingRoomMapper livingRoomMapper;
    @Resource
    private LivingRoomRecordMapper livingRoomRecordMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private LivingProviderCacheKeyBuilder cacheKeyBuilder;
    @Resource
    private ILivingRoomTxService livingRoomTxService;
    @DubboReference
    private ImRouterRpc imRouterRpc;

    //Get all online users connected in a room
    @Override
    public List<Long> queryUserIdByRoomId(LivingRoomReqDTO livingRoomReqDTO) {
        Integer roomId = livingRoomReqDTO.getRoomId();
        Integer appId = livingRoomReqDTO.getAppId();
        String cacheKey = cacheKeyBuilder.buildLivingRoomUserSet(roomId, appId);
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
    public void userOfflineHandler(ImOfflineDTO imOfflineDTO) {
        LOGGER.info("offline handler,imOfflineDTO is {}", imOfflineDTO);
        Long userId = imOfflineDTO.getUserId();
        Integer roomId = imOfflineDTO.getRoomId();
        Integer appId = imOfflineDTO.getAppId();
        String cacheKey = cacheKeyBuilder.buildLivingRoomUserSet(roomId, appId);
        redisTemplate.opsForSet().remove(cacheKey, userId);
        //When user get offline
        LivingRoomReqDTO roomReqDTO = new LivingRoomReqDTO();
        roomReqDTO.setRoomId(imOfflineDTO.getRoomId());
        roomReqDTO.setPkObjId(imOfflineDTO.getUserId());
        roomReqDTO.setAnchorId(imOfflineDTO.getUserId());
        this.offlinePk(roomReqDTO);
        //When broadcaster logout im server, close the live stream room as well.
        livingRoomTxService.closeLiving(roomReqDTO);
    }

    @Override
    public void userOnlineHandler(ImOnlineDTO imOnlineDTO) {
        LOGGER.info("online handler,imOnlineDTO is {}", imOnlineDTO);
        Long userId = imOnlineDTO.getUserId();
        Integer roomId = imOnlineDTO.getRoomId();
        Integer appId = imOnlineDTO.getAppId();
        String cacheKey = cacheKeyBuilder.buildLivingRoomUserSet(roomId, appId);
        //set connected user in room user list
        redisTemplate.opsForSet().add(cacheKey, userId);
        redisTemplate.expire(cacheKey, 12, TimeUnit.HOURS);
    }

    @Override
    public List<LivingRoomRespDTO> listAllLivingRoomFromDB(Integer type) {
        //Get at most 1000 rooms in the front page.
        LambdaQueryWrapper<LivingRoomPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LivingRoomPO::getStatus, CommonStatusEum.VALID_STATUS.getCode());
        queryWrapper.eq(LivingRoomPO::getType, type);
        //based on id desc order.
        queryWrapper.orderByDesc(LivingRoomPO::getId);
        queryWrapper.last("limit 1000");
        return ConvertBeanUtils.convertList(livingRoomMapper.selectList(queryWrapper), LivingRoomRespDTO.class);
    }

    @Override
    public PageWrapper<LivingRoomRespDTO> list(LivingRoomReqDTO livingRoomReqDTO) {
        //Get a page of rooms (20 to 30) in the front page.
        //The room is already cached and update every 1 second. So we can directly check Redis.
        String cacheKey = cacheKeyBuilder.buildLivingRoomList(livingRoomReqDTO.getType());
        int page = livingRoomReqDTO.getPage();
        int pageSize = livingRoomReqDTO.getPageSize();
        long total = redisTemplate.opsForList().size(cacheKey);
        List<Object> resultList = redisTemplate.opsForList().range(cacheKey, (page - 1) * pageSize, (page * pageSize));
        PageWrapper<LivingRoomRespDTO> pageWrapper = new PageWrapper<>();
        if (CollectionUtils.isEmpty(resultList)) {
            pageWrapper.setList(Collections.emptyList());
            pageWrapper.setHasNext(false);
            return pageWrapper;
        } else {
            List<LivingRoomRespDTO> livingRoomRespDTOS = ConvertBeanUtils.convertList(resultList, LivingRoomRespDTO.class);
            pageWrapper.setList(livingRoomRespDTOS);
            pageWrapper.setHasNext(page * pageSize < total);
            return pageWrapper;
        }
    }

    @Override
    public LivingRoomRespDTO queryByRoomId(Integer roomId) {
        String cacheKey = cacheKeyBuilder.buildLivingRoomObj(roomId);
        LivingRoomRespDTO queryResult = (LivingRoomRespDTO) redisTemplate.opsForValue().get(cacheKey);
        if (queryResult != null) {
            //Empty value cached.
            if (queryResult.getId() == null) {
                return null;
            }
            return queryResult;
        }
        LambdaQueryWrapper<LivingRoomPO> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(LivingRoomPO::getId, roomId);
        queryWrapper.eq(LivingRoomPO::getStatus, CommonStatusEum.VALID_STATUS.getCode());
        queryWrapper.last("limit 1");
        queryResult = ConvertBeanUtils.convert(livingRoomMapper.selectOne(queryWrapper), LivingRoomRespDTO.class);
        if (queryResult == null) {
            //Empty value cached.
            redisTemplate.opsForValue().set(cacheKey, new LivingRoomRespDTO(), 1, TimeUnit.MINUTES);
            return null;
        }
        if (LivingRoomTypeEnum.PK_LIVING_ROOM.getCode().equals(queryResult.getType())) {
            queryResult.setPkObjId(this.queryOnlinePkUserId(roomId));
        }
        redisTemplate.opsForValue().set(cacheKey, queryResult, 30, TimeUnit.MINUTES);
        return queryResult;
    }

    @Override
    public Integer startLivingRoom(LivingRoomReqDTO livingRoomReqDTO) {
        LivingRoomPO livingRoomPO = ConvertBeanUtils.convert(livingRoomReqDTO, LivingRoomPO.class);
        livingRoomPO.setStatus(CommonStatusEum.VALID_STATUS.getCode());
        livingRoomPO.setStartTime(new Date());
        livingRoomMapper.insert(livingRoomPO);
        String cacheKey = cacheKeyBuilder.buildLivingRoomObj(livingRoomPO.getId());
        //When the room is started
        redisTemplate.delete(cacheKey);
        return livingRoomPO.getId();
    }


    @Override
    public Long queryOnlinePkUserId(Integer roomId) {
        String cacheKey = cacheKeyBuilder.buildLivingOnlinePk(roomId);
        Object userId = redisTemplate.opsForValue().get(cacheKey);
        return userId != null ? Long.valueOf((int) userId) : null;
    }

    @Override
    public LivingPkRespDTO onlinePk(LivingRoomReqDTO livingRoomReqDTO) {
        LivingRoomRespDTO currentLivingRoom = this.queryByRoomId(livingRoomReqDTO.getRoomId());
        LivingPkRespDTO respDTO = new LivingPkRespDTO();
        respDTO.setOnlineStatus(false);
        if (currentLivingRoom.getAnchorId().equals(livingRoomReqDTO.getPkObjId())) {
            respDTO.setMsg("Anchor cannot enter pk");
            return respDTO;
        }
        String cacheKey = cacheKeyBuilder.buildLivingOnlinePk(livingRoomReqDTO.getRoomId());
        boolean tryOnline = redisTemplate.opsForValue().setIfAbsent(cacheKey, livingRoomReqDTO.getPkObjId(), 30, TimeUnit.HOURS);
        if (tryOnline) {
            List<Long> userIdList = this.queryUserIdByRoomId(livingRoomReqDTO);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("pkObjId", livingRoomReqDTO.getPkObjId());
            jsonObject.put("pkObjAvatar", "https://picdm.sunbangyan.cn/2023/08/29/w2qq1k.jpeg");
            batchSendImMsg(userIdList, ImMsgBizCodeEnum.LIVING_ROOM_PK_ONLINE.getCode(), jsonObject);
            respDTO.setMsg("pk connection success");
            respDTO.setOnlineStatus(false);
        } else {
            respDTO.setMsg("There is someone else in the pk. Try it later.");
        }
        return respDTO;
    }

    @Override
    public boolean offlinePk(LivingRoomReqDTO livingRoomReqDTO) {
        String cacheKey = cacheKeyBuilder.buildLivingOnlinePk(livingRoomReqDTO.getRoomId());
        return redisTemplate.delete(cacheKey);
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
}
