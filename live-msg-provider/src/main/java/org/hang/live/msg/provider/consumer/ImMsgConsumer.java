package org.hang.live.msg.provider.consumer;

import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.hang.live.common.interfaces.topic.ImCoreServerProviderTopicNames;
import org.hang.live.framework.mq.starter.properties.RocketMQConsumerProperties;
import org.hang.live.im.dto.ImMsgBody;
import org.hang.live.msg.provider.consumer.handler.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;


/**
 * @Author hang
 * @Date: Created in 15:04 2024/8/14
 * @Description
 */
@Component
public class ImMsgConsumer implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImMsgConsumer.class);
    @Resource
    private RocketMQConsumerProperties rocketMQConsumerProperties;
    @Resource
    private MessageHandler singleMessageHandler;

    //Deal with business message sent by the user.
    @Override
    public void afterPropertiesSet() throws Exception {
        DefaultMQPushConsumer mqPushConsumer = new DefaultMQPushConsumer();
        mqPushConsumer.setVipChannelEnabled(false);
        mqPushConsumer.setNamesrvAddr(rocketMQConsumerProperties.getNameSrv());
        mqPushConsumer.setConsumerGroup(rocketMQConsumerProperties.getGroupName() + "_" + ImMsgConsumer.class.getSimpleName());
        mqPushConsumer.setConsumeMessageBatchMaxSize(10);
        mqPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        mqPushConsumer.subscribe(ImCoreServerProviderTopicNames.QIYU_LIVE_IM_BIZ_MSG_TOPIC, "");
        mqPushConsumer.setMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            //Apply business logics to the message.(Currently only support broadcasting message)
            for (MessageExt msg : msgs) {
                ImMsgBody imMsgBody = JSON.parseObject(new String(msg.getBody()), ImMsgBody.class);
                singleMessageHandler.onMsgReceive(imMsgBody);
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        mqPushConsumer.start();
        LOGGER.info("MQ consumer for business topic is started,namesrv is {}", rocketMQConsumerProperties.getNameSrv());
    }
}
