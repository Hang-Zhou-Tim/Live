package org.hang.live.gift.provider.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.hang.live.stream.room.interfaces.dto.LiveStreamRoomReqDTO;
import org.hang.live.stream.room.interfaces.rpc.ILiveStreamRoomRPC;
import org.hang.live.user.payment.interfaces.IAccountBalanceRPC;
import org.hang.live.common.interfaces.topic.GiftProviderTopicNames;
import org.hang.live.common.interfaces.utils.ListUtils;
import org.hang.live.common.redis.configuration.key.GiftProviderCacheKeyBuilder;
import org.hang.live.gift.constants.RedPacketStatusEnum;
import org.hang.live.gift.dto.RedPacketConfigReqDTO;
import org.hang.live.gift.dto.SnatchRedPacketDTO;
import org.hang.live.gift.provider.dao.mapper.IRedPacketConfigMapper;
import org.hang.live.gift.provider.dao.po.RedPacketConfigPO;
import org.hang.live.gift.provider.service.IRedPacketConfigService;
import org.hang.live.gift.provider.service.bo.SendRedPacketBO;
import org.hang.live.im.core.server.interfaces.constants.AppIdEnum;
import org.hang.live.im.core.server.interfaces.dto.ImMsgBody;
import org.hang.live.im.server.router.interfaces.constants.ImMsgBizCodeEnum;
import org.hang.live.im.server.router.interfaces.rpc.ImRouterRPC;
import org.hang.live.stream.room.interfaces.rpc.ILiveStreamRoomRPC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
@Service
public class RedPacketConfigServiceImpl implements IRedPacketConfigService {

    @Resource
    private IRedPacketConfigMapper redPacketConfigMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private GiftProviderCacheKeyBuilder cacheKeyBuilder;

    @DubboReference
    private ImRouterRPC routerRPC;
    @DubboReference
    private ILiveStreamRoomRPC liveStreamRoomRPC;
    @DubboReference
    private IAccountBalanceRPC accountBalanceRPC;

    @Resource
    private MQProducer mqProducer;

    private static final Logger LOGGER = LoggerFactory.getLogger(RedPacketConfigServiceImpl.class);


    @Override
    public RedPacketConfigPO queryByAnchorId(Long anchorId) {
        LambdaQueryWrapper<RedPacketConfigPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RedPacketConfigPO::getAnchorId, anchorId);
        //In normal environment, anchor can only start one red packet event per application of event to the Administrator.
        //After red packet rain event finished, the status should be "ended", and anchor cannot activate new event based on previous application.
        //In test environment, I want the anchor can send red packet without that application, so I comment out this line to un-check code.
        //queryWrapper.eq(RedPacketConfigPO::getStatus, RedPacketStatusEnum.WAIT.getCode());
        queryWrapper.orderByDesc(RedPacketConfigPO::getCreateTime);
        queryWrapper.last("limit 1");
        return redPacketConfigMapper.selectOne(queryWrapper);
    }

    @Override
    public RedPacketConfigPO queryByConfigCode(String code) {
        LambdaQueryWrapper<RedPacketConfigPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RedPacketConfigPO::getConfigCode, code);
        queryWrapper.eq(RedPacketConfigPO::getStatus, RedPacketStatusEnum.IS_PREPARED.getCode());
        queryWrapper.orderByDesc(RedPacketConfigPO::getCreateTime);
        queryWrapper.last("limit 1");
        return redPacketConfigMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean addOne(RedPacketConfigPO redPacketConfigPO) {
        redPacketConfigPO.setConfigCode(UUID.randomUUID().toString());
        return redPacketConfigMapper.insert(redPacketConfigPO) > 0;
    }

    @Override
    public boolean updateById(RedPacketConfigPO redPacketConfigPO) {
        return redPacketConfigMapper.updateById(redPacketConfigPO) > 0;
    }

    @Override
    public boolean prepareRedPacket(Long anchorId) {
        // Avoid anchor with no right to start red packet rain.
        RedPacketConfigPO redPacketConfigPO = this.queryByAnchorId(anchorId);
        if (redPacketConfigPO == null) {
            return false;
        }
        // avoid anchor to frequent click on button and generate duplicate event.
        Boolean isLock = redisTemplate.opsForValue().setIfAbsent(cacheKeyBuilder.buildRedPacketInitLock(redPacketConfigPO.getConfigCode()), 1, 3L, TimeUnit.SECONDS);
        if (Boolean.FALSE.equals(isLock)) {
            return false;
        }
        // get total price and count to generate list of red packet prices.
        Integer totalPrice = redPacketConfigPO.getTotalPrice();
        Integer totalCount = redPacketConfigPO.getTotalCount();
        List<Integer> priceList = this.createRedPacketPriceList(totalPrice, totalCount);
        String cacheKey = cacheKeyBuilder.buildRedPacketList(redPacketConfigPO.getConfigCode());
        // split the price list to avoid long-processing time on Redis.
        // since this is in distributed environment, Redis responsiveness is important.
        List<List<Integer>> splitPriceList = ListUtils.splistList(priceList, 100);
        for (List<Integer> priceItemList : splitPriceList) {
            redisTemplate.opsForList().leftPushAll(cacheKey, priceItemList.toArray());
        }
        // Set Status to Is_Prepared
        redPacketConfigPO.setStatus(RedPacketStatusEnum.IS_PREPARED.getCode());
        this.updateById(redPacketConfigPO);
        // Set that preparation of red packet rain succeeds.
        redisTemplate.opsForValue().set(cacheKeyBuilder.buildRedPacketPrepareSuccess(redPacketConfigPO.getConfigCode()), 1, 1L, TimeUnit.DAYS);
        return true;

    }

    /**
     * To maintain even distribution, I utilised algorithms below:
     *     for each price It takes, generate random number between [0, 2 * totalPrice / totalCount]
     */
    private List<Integer> createRedPacketPriceList(Integer totalPrice, Integer totalCount) {
        List<Integer> redPacketPriceList = new ArrayList<>();
        for (int i = 0; i < totalCount; i++) {
            if (i + 1 == totalCount) {
                // If this is the last red packet, then add the remaining price.
                redPacketPriceList.add(totalPrice);
                break;
            }
            // why 2?
            // This avoids the generation of price at later stage is bigger than early stage.
            int maxLimit = (totalPrice / (totalCount - i)) * 2;
            int currentPrice = ThreadLocalRandom.current().nextInt(1, maxLimit);
            totalPrice -= currentPrice;
            redPacketPriceList.add(currentPrice);
        }
        return redPacketPriceList;
    }
    
    @Override
    public SnatchRedPacketDTO snatchRedPacket(RedPacketConfigReqDTO redPacketConfigReqDTO) {
        // Use code as key to prevent users to guess red packet address and get red packet from other anchor's room.
        String code = redPacketConfigReqDTO.getRedPacketConfigCode();
        // Get red packet list.
        String cacheKey = cacheKeyBuilder.buildRedPacketList(code);
        Object priceObj = redisTemplate.opsForList().rightPop(cacheKey);
        if (priceObj == null) {
            return null;
        }
        Integer price = (Integer) priceObj;
        // send MQ message for async handling of time-consuming processing of red packet event like Database I/O.
        SendRedPacketBO sendRedPacketBO = new SendRedPacketBO();
        sendRedPacketBO.setPrice(price);
        sendRedPacketBO.setReqDTO(redPacketConfigReqDTO);
        Message message = new Message();
        message.setTopic(GiftProviderTopicNames.SNATCH_RED_PACKET);
        message.setBody(JSON.toJSONBytes(sendRedPacketBO));
        try {
            SendResult sendResult = mqProducer.send(message);
            LOGGER.info("[insertOne] sendResult is {}", sendResult);
        } catch (Exception e) {
            return new SnatchRedPacketDTO(null, "Already Snatched T_T");
        }
        return new SnatchRedPacketDTO(price, "Snatched " + price + "¥!");
    }

    
    @Override
    public Boolean startRedPacket(RedPacketConfigReqDTO reqDTO) {
        String code = reqDTO.getRedPacketConfigCode();

        // If the red packet list is not prepared, return false.
        if (Boolean.FALSE.equals(redisTemplate.hasKey(cacheKeyBuilder.buildRedPacketPrepareSuccess(code)))) {
            LOGGER.info("This red packet rain event is not prepared before.");
            return false;
        }
        // If the red packet rain is already started, then return false.
        String notifySuccessCacheKey = cacheKeyBuilder.buildRedPacketNotify(code);
        if (Boolean.TRUE.equals(redisTemplate.hasKey(notifySuccessCacheKey))) {
            LOGGER.info("This red packet rain event is already sent by users.");
            return false;
        }
        redisTemplate.opsForValue().set(notifySuccessCacheKey, 1, 1L, TimeUnit.MINUTES);
        // Notify all users in the room to snatch the red packet rain.
        RedPacketConfigPO redPacketConfigPO = this.queryByConfigCode(code);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("redPacketConfig", JSON.toJSONString(redPacketConfigPO));
        LiveStreamRoomReqDTO livingRoomReqDTO = new LiveStreamRoomReqDTO();
        livingRoomReqDTO.setRoomId(reqDTO.getRoomId());
        livingRoomReqDTO.setAppId(AppIdEnum.LIVE_BIZ.getCode());
        List<Long> userIdList = liveStreamRoomRPC.queryUserIdByRoomId(livingRoomReqDTO);
        if (CollectionUtils.isEmpty(userIdList)){
            LOGGER.info("There is no one else in the room. Please reconnect to this room.");
            return false;
        }

        LOGGER.info("Sending Red Packet Rain Event Notification to All Users in This Rooms.");
        this.batchSendImMessage(userIdList, ImMsgBizCodeEnum.RED_PACKET_CONFIG.getCode(), jsonObject);
        LOGGER.info("Sending Finished");
        // Set the status of red packet rain to send.
        redPacketConfigPO.setStatus(RedPacketStatusEnum.IS_SEND.getCode());
        this.updateById(redPacketConfigPO);
        return true;
    }

    /**
     * batch send im message
     */
    private void batchSendImMessage(List<Long> userIdList, Integer bizCode, JSONObject jsonObject) {
        List<ImMsgBody> imMsgBodies = new ArrayList<>();

        userIdList.forEach(userId -> {
            ImMsgBody imMsgBody = new ImMsgBody();
            imMsgBody.setAppId(AppIdEnum.LIVE_BIZ.getCode());
            imMsgBody.setBizCode(bizCode);
            imMsgBody.setData(jsonObject.toJSONString());
            imMsgBody.setUserId(userId);
            imMsgBodies.add(imMsgBody);
        });
        routerRPC.batchSendMsg(imMsgBodies);
    }

    @Override
    public void snatchedRedPacket(RedPacketConfigReqDTO reqDTO, Integer price) {
        String code = reqDTO.getRedPacketConfigCode();
        String totalGetCountCacheKey = cacheKeyBuilder.buildRedPacketTotalGetCount(code);
        String totalGetPriceCacheKey = cacheKeyBuilder.buildRedPacketTotalGetPrice(code);
        // record the total price users get from red packet rain
        redisTemplate.opsForValue().increment(cacheKeyBuilder.buildUserTotalGetPrice(reqDTO.getUserId()), price);
        // record total price earned and count by user from that rain
        // using hash to avoid key collision (because we used %100 to the key, different red packet rain can use it)
        redisTemplate.opsForHash().increment(totalGetCountCacheKey, code, 1);
        redisTemplate.expire(totalGetCountCacheKey, 1L, TimeUnit.DAYS);
        redisTemplate.opsForHash().increment(totalGetPriceCacheKey, code, price);
        redisTemplate.expire(totalGetPriceCacheKey, 1L, TimeUnit.DAYS);
        // increment the user's balance based on the price snatched by the user.
        accountBalanceRPC.incrementBalance(reqDTO.getUserId(), price);
        // increment the total price and total snatched amount of red packet in this red packet rain.
        redPacketConfigMapper.incrTotalGetPrice(code, price);
        redPacketConfigMapper.incrTotalGetCount(code);
    }
}