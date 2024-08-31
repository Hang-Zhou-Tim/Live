package org.hang.live.gift.provider.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.hang.live.common.redis.configuration.key.GiftProviderCacheKeyBuilder;
import org.hang.live.stream.room.interfaces.dto.LiveStreamRoomReqDTO;
import org.hang.live.stream.room.interfaces.dto.LiveStreamRoomRespDTO;
import org.hang.live.stream.room.interfaces.rpc.ILiveStreamRoomRPC;
import org.hang.live.user.payment.dto.TransactionTurnoverReqDTO;
import org.hang.live.user.payment.dto.TransactionTurnoverRespDTO;
import org.hang.live.user.payment.interfaces.IAccountBalanceRPC;
import org.hang.live.common.interfaces.dto.SendGiftMq;
import org.hang.live.common.interfaces.topic.GiftProviderTopicNames;
import org.hang.live.common.mq.configuration.properties.RocketMQConsumerProperties;
import org.hang.live.gift.constants.SendGiftTypeEnum;
import org.hang.live.im.core.server.interfaces.constants.AppIdEnum;
import org.hang.live.im.core.server.interfaces.dto.ImMsgBody;
import org.hang.live.im.server.router.interfaces.constants.ImMsgBizCodeEnum;
import org.hang.live.im.server.router.interfaces.rpc.ImRouterRPC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description RocketMQ Consumer that Listen Sending Gifts
 */
@Configuration
public class SendPKGiftConsumer implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendPKGiftConsumer.class);

    private static final Long PK_INIT_NUM = 50L;
    private static final Long PK_MAX_NUM = 100L;
    private static final Long PK_MIN_NUM = 0L;
    private String LUA_SCRIPT =
            "if (redis.call('exists', KEYS[1])) == 1 then " +
                    " local currentNum=redis.call('get',KEYS[1]) " +
                    " if (tonumber(currentNum)<=tonumber(ARGV[2]) and tonumber(currentNum)>=tonumber(ARGV[3])) then " +
                    " return redis.call('incrby',KEYS[1],tonumber(ARGV[4])) " +
                    " else return currentNum end " +
                    "else " +
                    "redis.call('set', KEYS[1], tonumber(ARGV[1])) " +
                    "redis.call('EXPIRE', KEYS[1], 3600 * 12) " +
                    "return redis.call('incrby',KEYS[1],tonumber(ARGV[4])) end";

    @Resource
    private RocketMQConsumerProperties rocketMQConsumerProperties;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private GiftProviderCacheKeyBuilder cacheKeyBuilder;
    @DubboReference
    private IAccountBalanceRPC accountBalanceRpc;
    @DubboReference
    private ILiveStreamRoomRPC liveStreamRoomRPC;
    @DubboReference
    private ImRouterRPC routerRPC;

    @Override
    public void afterPropertiesSet() throws Exception {
        DefaultMQPushConsumer mqPushConsumer = new DefaultMQPushConsumer();
        mqPushConsumer.setVipChannelEnabled(false);
        mqPushConsumer.setNamesrvAddr(rocketMQConsumerProperties.getNameSrv());
        mqPushConsumer.setConsumerGroup(rocketMQConsumerProperties.getGroupName() + "_" + SendPKGiftConsumer.class.getSimpleName());
        mqPushConsumer.setConsumeMessageBatchMaxSize(10);
        mqPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        //Listen the situation when a user sends a gift.
        mqPushConsumer.subscribe(GiftProviderTopicNames.SEND_PK_GIFT, "");
        mqPushConsumer.setMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            for (MessageExt msg : msgs) {
                SendGiftMq sendGiftMq = JSON.parseObject(new String(msg.getBody()), SendGiftMq.class);
                String mqConsumerKey = cacheKeyBuilder.buildGiftConsumeKey(sendGiftMq.getUuid());
                //To avoid side effect of MQ shutdown and reconnect(which re-consume some messages, we check whether it is consumed before.)
                boolean lockStatus = redisTemplate.opsForValue().setIfAbsent(mqConsumerKey, -1, 5, TimeUnit.MINUTES);
                if (!lockStatus) {
                    continue;
                }
                TransactionTurnoverReqDTO tradeReqDTO = new TransactionTurnoverReqDTO();
                tradeReqDTO.setUserId(sendGiftMq.getUserId());
                tradeReqDTO.setNum(sendGiftMq.getPrice());
                TransactionTurnoverRespDTO tradeRespDTO = accountBalanceRpc.consumeForSendGift(tradeReqDTO);
                JSONObject jsonObject = new JSONObject();
                //If the account balance is deduced successfully, send the gift message to all users in room, so they can view the animation.
                if (tradeRespDTO.isSuccess()) {
                    Long receiverId = sendGiftMq.getReceiverId();
                    this.sendOnlinePKGiftNotification(jsonObject, sendGiftMq, receiverId);
                } else {
                    //Tell the user that gift is failed to send.
                    jsonObject.put("msg", tradeRespDTO.getMsg());
                    this.sendSingleImNotification(sendGiftMq.getUserId(), ImMsgBizCodeEnum.LIVING_ROOM_SEND_GIFT_FAIL.getCode(), jsonObject);
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        mqPushConsumer.start();
        LOGGER.info("RocketMQ consuemr that listens sending pk gift is initialised,namesrv is {}", rocketMQConsumerProperties.getNameSrv());
    }

/**
     * Send IM Message.
     *
     * @param userId
     * @param bizCode
     * @param jsonObject
     */
    private void sendSingleImNotification(Long userId, int bizCode, JSONObject jsonObject) {
        ImMsgBody imMsgBody = new ImMsgBody();
        imMsgBody.setAppId(AppIdEnum.LIVE_BIZ.getCode());
        imMsgBody.setBizCode(bizCode);
        imMsgBody.setUserId(userId);
        imMsgBody.setData(jsonObject.toJSONString());
        routerRPC.sendMsg(imMsgBody);
    }

    private void sendOnlinePKGiftNotification(JSONObject jsonObject, SendGiftMq sendGiftMq, Long receiverId) {
        //1. update pk progress bar.
            // Default value for two sides in PK gift bar is 50:50.
            // When users send gift of value 50¥ to left user, then the pk bar will be 55:45.
            // I only record the progress of left hand-side in Redis.
        //2. notify gift animation.
        Integer roomId = sendGiftMq.getRoomId();
        String isOverCacheKey = cacheKeyBuilder.buildPKLiveStreamIsOver(roomId);
        if (redisTemplate.hasKey(isOverCacheKey)) {
            return;
        }
        LiveStreamRoomRespDTO respDTO = liveStreamRoomRPC.queryByRoomId(roomId);
        Long pkObjId = liveStreamRoomRPC.queryOnlinePkUserId(roomId);
        if (pkObjId == null || respDTO == null || respDTO.getAnchorId() == null) {
            return;
        }
        Long pkUserId = respDTO.getAnchorId();
        Long pkNum = 0L;
        String pkNumKey = cacheKeyBuilder.buildPKLiveStreamKey(roomId);
        //Set LUA Script to Atomically Update PK Progress, This ensures Atomic Process under Situation of High Concurrency
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript();
        redisScript.setScriptText(LUA_SCRIPT);
        redisScript.setResultType(Long.class);
        Long sendGiftSeqNum = System.currentTimeMillis();
        if (pkUserId.equals(receiverId)) {
            Integer moveStep = sendGiftMq.getPrice() / 10;
            pkNum = this.redisTemplate.execute(redisScript, Collections.singletonList(pkNumKey), PK_INIT_NUM, PK_MAX_NUM, PK_MIN_NUM, moveStep);
            if (PK_MAX_NUM <= pkNum) {
                jsonObject.put("winnerId", pkUserId);
            }
        } else if (pkObjId.equals(receiverId)) {
            Integer moveStep = sendGiftMq.getPrice() / 10 * -1;
            pkNum = this.redisTemplate.execute(redisScript, Collections.singletonList(pkNumKey), PK_INIT_NUM, PK_MAX_NUM, PK_MIN_NUM, moveStep);
            if (PK_MIN_NUM >= pkNum) {
                this.redisTemplate.opsForValue().set(cacheKeyBuilder.buildPKLiveStreamIsOver(roomId),-1);
                jsonObject.put("winnerId", pkObjId);
            }
        }
        jsonObject.put("receiverId", sendGiftMq.getReceiverId());
        jsonObject.put("sendGiftSeqNum", sendGiftSeqNum);
        jsonObject.put("pkNum", pkNum);
        jsonObject.put("url", sendGiftMq.getUrl());
        LiveStreamRoomReqDTO livingRoomReqDTO = new LiveStreamRoomReqDTO();
        livingRoomReqDTO.setRoomId(roomId);
        livingRoomReqDTO.setAppId(AppIdEnum.LIVE_BIZ.getCode());
        List<Long> userIdList = liveStreamRoomRPC.queryUserIdByRoomId(livingRoomReqDTO);
        this.sendImNotificationInBatch(userIdList, ImMsgBizCodeEnum.LIVING_ROOM_PK_SEND_GIFT_SUCCESS, jsonObject);
    }

    /**
     * Send Batched Message.
     *
     * @param userIdList
     * @param imMsgBizCodeEnum
     * @param jsonObject
     */
    private void sendImNotificationInBatch(List<Long> userIdList, ImMsgBizCodeEnum imMsgBizCodeEnum, JSONObject jsonObject) {
        List<ImMsgBody> imMsgBodies = userIdList.stream().map(userId -> {
            ImMsgBody imMsgBody = new ImMsgBody();
            imMsgBody.setAppId(AppIdEnum.LIVE_BIZ.getCode());
            imMsgBody.setBizCode(imMsgBizCodeEnum.getCode());
            imMsgBody.setUserId(userId);
            imMsgBody.setData(jsonObject.toJSONString());
            return imMsgBody;
        }).collect(Collectors.toList());
        routerRPC.batchSendMsg(imMsgBodies);
    }
}
