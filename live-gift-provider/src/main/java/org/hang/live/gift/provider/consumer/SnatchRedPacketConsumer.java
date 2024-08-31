package org.hang.live.gift.provider.consumer;

import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.hang.live.common.interfaces.topic.GiftProviderTopicNames;
import org.hang.live.common.mq.configuration.properties.RocketMQConsumerProperties;
import org.hang.live.gift.provider.service.IRedPacketConfigService;
import org.hang.live.gift.provider.service.bo.SendRedPacketBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * RocketMQ consumer dealing with snatch red packet
 */
@Component
public class SnatchRedPacketConsumer implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(SnatchRedPacketConsumer.class);
    @Resource
    private IRedPacketConfigService redPacketConfigService;
    @Resource
    private RocketMQConsumerProperties rocketMQConsumerProperties;

    public void startRedPacketReceiver() throws Exception {
        DefaultMQPushConsumer mqPushConsumer = new DefaultMQPushConsumer();
        mqPushConsumer.setVipChannelEnabled(false);
        mqPushConsumer.setNamesrvAddr(rocketMQConsumerProperties.getNameSrv());
        mqPushConsumer.setConsumerGroup(rocketMQConsumerProperties.getGroupName() + "_" + SnatchRedPacketConsumer.class.getSimpleName());
        mqPushConsumer.setConsumeMessageBatchMaxSize(10);
        mqPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        mqPushConsumer.subscribe(GiftProviderTopicNames.SNATCH_RED_PACKET, "");
        mqPushConsumer.setMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            for (MessageExt msg : msgs) {
                try {
                    SendRedPacketBO sendRedPacketBO = JSON.parseObject(new String(msg.getBody()), SendRedPacketBO.class);
                    redPacketConfigService.snatchedRedPacket(sendRedPacketBO.getReqDTO(), sendRedPacketBO.getPrice());
                    LOGGER.info("[SnatchRedPacketConsumer] SnatchRedPacketConsumer success");
                } catch (Exception e) {
                    LOGGER.error("[SnatchRedPacketConsumer] SnatchRedPacketConsumer error, mqBody is {}", msg);
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        mqPushConsumer.start();
        LOGGER.info("RocketMQ consumer that listens snatching red packet event is started,namesrv is {}", rocketMQConsumerProperties.getNameSrv());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        startRedPacketReceiver();
    }
}