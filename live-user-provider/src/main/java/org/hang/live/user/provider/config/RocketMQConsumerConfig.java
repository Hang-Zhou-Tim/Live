package org.hang.live.user.provider.config;

import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.hang.live.common.redis.configuration.key.UserProviderCacheKeyBuilder;
// import org.hang.live.common.interfaces.topic.UserProviderTopicNames;
//import org.hang.live.user.constants.CacheAsyncDeleteCode;
//import org.hang.live.user.dto.UserCacheAsyncDeleteDTO;
import org.hang.live.user.constants.CacheAsyncDeleteCode;
import org.hang.live.user.dto.UserCacheAsyncDeleteDTO;
import org.hang.live.user.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/21
 * @Description
 */
@Configuration
public class RocketMQConsumerConfig implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(RocketMQConsumerConfig.class);

    @Resource
    private RocketMQConsumerProperties rocketMQConsumerProperties;
    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Resource
    private UserProviderCacheKeyBuilder cacheKeyBuilder;

    @Override
    public void afterPropertiesSet() throws Exception {
        DefaultMQPushConsumer mqPushConsumer = new DefaultMQPushConsumer();
        mqPushConsumer.setVipChannelEnabled(false);
        mqPushConsumer.setNamesrvAddr(rocketMQConsumerProperties.getNameSrv());
        mqPushConsumer.setConsumerGroup(rocketMQConsumerProperties.getGroupName() + "_" + RocketMQConsumerConfig.class.getSimpleName());
        mqPushConsumer.setConsumeMessageBatchMaxSize(1);
        mqPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        mqPushConsumer.subscribe("user-update-cache","*");
        mqPushConsumer.setMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                String json = new String(msgs.get(0).getBody());
                UserDTO userDTO = JSON.parseObject(json, UserDTO.class);
                if(userDTO == null || userDTO.getUserId() == null){
                    LOGGER.error("User id is null, {}", json);
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
                redisTemplate.delete(cacheKeyBuilder.buildUserInfoKey(userDTO.getUserId()));
                LOGGER.debug("Delayed deletion is finished, {}", userDTO);

                UserCacheAsyncDeleteDTO userCacheAsyncDeleteDTO = JSON.parseObject(json, UserCacheAsyncDeleteDTO.class);
                if (CacheAsyncDeleteCode.USER_INFO_DELETE.getCode() == userCacheAsyncDeleteDTO.getCode()) {
                    Long userId = JSON.parseObject(userCacheAsyncDeleteDTO.getJson()).getLong("userId");
                    redisTemplate.delete(cacheKeyBuilder.buildUserInfoKey(userId));
                    LOGGER.info("Delay deleting the user id，userId is {}",userId);
                } else if (CacheAsyncDeleteCode.USER_TAG_DELETE.getCode() == userCacheAsyncDeleteDTO.getCode()) {
                    Long userId = JSON.parseObject(userCacheAsyncDeleteDTO.getJson()).getLong("userId");
                    redisTemplate.delete(cacheKeyBuilder.buildTagKey(userId));
                    LOGGER.info("Delay Deleting the user tag，userId is {}",userId);
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        mqPushConsumer.start();
        LOGGER.info("Message Queue Consumer is Started ,namesrv is {}", rocketMQConsumerProperties.getNameSrv());
    }


}
