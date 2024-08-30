package org.hang.live.gift.provider.consumer;

import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.hang.live.common.redis.configuration.key.GiftProviderCacheKeyBuilder;
import org.hang.live.common.interfaces.topic.GiftProviderTopicNames;
import org.hang.live.common.mq.configuration.properties.RocketMQConsumerProperties;
import org.hang.live.gift.provider.service.bo.GiftCacheRemoveBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description RocketMQ Consumer that Removed Old Gift Config
 */
@Configuration
public class GiftConfigCacheConsumer implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(GiftConfigCacheConsumer.class);

    @Resource
    private RocketMQConsumerProperties rocketMQConsumerProperties;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Resource
    private GiftProviderCacheKeyBuilder cacheKeyBuilder;

    @Override
    public void afterPropertiesSet() throws Exception {
        DefaultMQPushConsumer mqPushConsumer = new DefaultMQPushConsumer();
        mqPushConsumer.setVipChannelEnabled(false);
        mqPushConsumer.setNamesrvAddr(rocketMQConsumerProperties.getNameSrv());
        mqPushConsumer.setConsumerGroup(rocketMQConsumerProperties.getGroupName() + "_" + GiftConfigCacheConsumer.class.getSimpleName());
        mqPushConsumer.setConsumeMessageBatchMaxSize(10);
        mqPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        //Listen the event when a gift is updated or removed.
        mqPushConsumer.subscribe(GiftProviderTopicNames.REMOVE_GIFT_CACHE, "");
        mqPushConsumer.setMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            for (MessageExt msg : msgs) {
                GiftCacheRemoveBO giftCacheRemoveBO = JSON.parseObject(new String(msg.getBody()), GiftCacheRemoveBO.class);
                if(giftCacheRemoveBO.isRemoveListCache()) {
                    redisTemplate.delete(cacheKeyBuilder.buildGiftListCacheKey());
                }
                if(giftCacheRemoveBO.getGiftId()>0) {
                    redisTemplate.delete(cacheKeyBuilder.buildGiftConfigCacheKey(giftCacheRemoveBO.getGiftId()));
                }
                LOGGER.info("[GiftConfigCacheConsumer] remove gift cache");
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        mqPushConsumer.start();
        LOGGER.info("RocketMQ that deletes gift cache initialised successfully,namesrv is {}", rocketMQConsumerProperties.getNameSrv());
    }
}
