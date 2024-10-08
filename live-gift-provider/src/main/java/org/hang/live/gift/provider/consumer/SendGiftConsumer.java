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
public class SendGiftConsumer implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendGiftConsumer.class);

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
        mqPushConsumer.setConsumerGroup(rocketMQConsumerProperties.getGroupName() + "_" + SendGiftConsumer.class.getSimpleName());
        mqPushConsumer.setConsumeMessageBatchMaxSize(10);
        mqPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        //Listen the situation when a user sends a gift.
        mqPushConsumer.subscribe(GiftProviderTopicNames.SEND_GIFT, "");
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
                    //Notify all users.
                    jsonObject.put("url", sendGiftMq.getUrl());
                    LiveStreamRoomReqDTO reqDTO = new LiveStreamRoomReqDTO();
                    reqDTO.setAppId(AppIdEnum.LIVE_BIZ.getCode());
                    reqDTO.setRoomId(sendGiftMq.getRoomId());
                    List<Long> userIdList = liveStreamRoomRPC.queryUserIdByRoomId(reqDTO);
                    this.sendImNotificationInBatch(userIdList, ImMsgBizCodeEnum.LIVING_ROOM_SEND_GIFT_SUCCESS, jsonObject);
                } else {
                    //Tell the user that gift is failed to send.
                    jsonObject.put("msg", tradeRespDTO.getMsg());
                    this.sendSingleImNotification(sendGiftMq.getUserId(), ImMsgBizCodeEnum.LIVING_ROOM_SEND_GIFT_FAIL.getCode(), jsonObject);
                }
//                LOGGER.info("[SendGiftConsumer] msg is {}", msg);
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        mqPushConsumer.start();
        LOGGER.info("RocketMQ consuemr that listens sending gift is initialised,namesrv is {}", rocketMQConsumerProperties.getNameSrv());
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
