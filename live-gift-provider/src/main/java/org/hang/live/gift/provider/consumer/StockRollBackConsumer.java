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
import org.hang.live.gift.provider.service.bo.RollBackStockBO;
import org.hang.live.gift.provider.service.ISkuStockInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/30
 * @Description
 */
@Component
public class StockRollBackConsumer implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(StockRollBackConsumer.class);

    @Resource
    private ISkuStockInfoService skuStockInfoService;
    @Resource
    private RocketMQConsumerProperties rocketMQConsumerProperties;

    public void stockRollBack() throws Exception {
        DefaultMQPushConsumer mqPushConsumer = new DefaultMQPushConsumer();
        mqPushConsumer.setVipChannelEnabled(false);
        mqPushConsumer.setNamesrvAddr(rocketMQConsumerProperties.getNameSrv());
        mqPushConsumer.setConsumerGroup(rocketMQConsumerProperties.getGroupName() + "_" + StockRollBackConsumer.class.getSimpleName());
        mqPushConsumer.setConsumeMessageBatchMaxSize(10);
        mqPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        mqPushConsumer.subscribe(GiftProviderTopicNames.ROLL_BACK_STOCK, "");
        mqPushConsumer.setMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            for (MessageExt msg : msgs) {
                try {
                    RollBackStockBO rollBackStockBO = JSON.parseObject(new String(msg.getBody()), RollBackStockBO.class);
                    skuStockInfoService.stockRollBackHandler(rollBackStockBO);
                    LOGGER.info("[StockRollBackConsumer] rollback stock success");
                } catch (Exception e) {
                    LOGGER.error("[StockRollBackConsumer] rollback stock failed, mqBody is {}", msg);
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        mqPushConsumer.start();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        stockRollBack();
    }
    
}