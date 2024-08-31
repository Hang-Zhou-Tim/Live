package org.hang.live.gift.provider.consumer;

import jakarta.annotation.Resource;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.hang.live.common.interfaces.topic.GiftProviderTopicNames;
import org.hang.live.common.mq.configuration.properties.RocketMQConsumerProperties;
import org.hang.live.gift.interfaces.ISkuStockInfoRPC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
@Component
public class StartLiveStreamRoomConsumer implements InitializingBean {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(StartLiveStreamRoomConsumer.class);
    @Resource
    private ISkuStockInfoRPC skuStockInfoRpc;

    @Resource
    private RocketMQConsumerProperties rocketMQConsumerProperties;

    public void prepareStock() throws Exception {
        DefaultMQPushConsumer mqPushConsumer = new DefaultMQPushConsumer();
        mqPushConsumer.setVipChannelEnabled(false);
        mqPushConsumer.setNamesrvAddr(rocketMQConsumerProperties.getNameSrv());
        mqPushConsumer.setConsumerGroup(rocketMQConsumerProperties.getGroupName() + "_" + StartLiveStreamRoomConsumer.class.getSimpleName());
        mqPushConsumer.setConsumeMessageBatchMaxSize(10);
        mqPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        mqPushConsumer.subscribe(GiftProviderTopicNames.PREPARE_STOCK, "");
        mqPushConsumer.setMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            for (MessageExt msg : msgs) {
                try {
                    Long anchorId = Long.valueOf(new String(msg.getBody()));
                    boolean isSuccess = skuStockInfoRpc.prepareStockInfo(anchorId);
                    if (isSuccess) {
                        LOGGER.info("[StartLivingRoomConsumer] Synchronise Stock Info to Redis Success! Ready to Show Stock Info for Anchor ：{}", anchorId);
                    }
                } catch (Exception e) {
                    LOGGER.error("[StartLivingRoomConsumer] Failed to Synchronise Stock Info to Redis, mqBody is {}", msg);
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        mqPushConsumer.start();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        prepareStock();
    }

}