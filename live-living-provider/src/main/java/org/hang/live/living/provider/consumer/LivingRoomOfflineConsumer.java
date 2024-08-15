package org.hang.live.living.provider.consumer;

import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.hang.live.living.provider.service.ILivingRoomService;
import org.hang.live.common.interfaces.topic.ImCoreServerProviderTopicNames;
import org.hang.live.framework.mq.starter.properties.RocketMQConsumerProperties;
import org.hang.live.im.core.server.interfaces.dto.ImOfflineDTO;
import org.hang.live.im.core.server.interfaces.dto.ImOnlineDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;


/**
 * @Author idea
 * @Date: Created in 15:04 2023/7/11
 * @Description
 */
@Component
public class LivingRoomOfflineConsumer implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(LivingRoomOfflineConsumer.class);
    @Resource
    private RocketMQConsumerProperties rocketMQConsumerProperties;
    @Resource
    private ILivingRoomService livingRoomService;

    @Override
    public void afterPropertiesSet() throws Exception {
        DefaultMQPushConsumer mqPushConsumer = new DefaultMQPushConsumer();
        mqPushConsumer.setVipChannelEnabled(false);
        mqPushConsumer.setNamesrvAddr(rocketMQConsumerProperties.getNameSrv());
        mqPushConsumer.setConsumerGroup(rocketMQConsumerProperties.getGroupName() + "_" + LivingRoomOfflineConsumer.class.getSimpleName());
        //consume at most 10 message at once.
        mqPushConsumer.setConsumeMessageBatchMaxSize(10);
        mqPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        //Listen room offline topics sent by core server.
        mqPushConsumer.subscribe(ImCoreServerProviderTopicNames.IM_OFFLINE_TOPIC, "");
        mqPushConsumer.setMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            for (MessageExt msg : msgs) {
                livingRoomService.userOfflineHandler(JSON.parseObject(new String(msg.getBody()), ImOfflineDTO.class));
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        mqPushConsumer.start();
        LOGGER.info("MQ consumer for user online topic is started,namesrv is {}", rocketMQConsumerProperties.getNameSrv());
    }
}
