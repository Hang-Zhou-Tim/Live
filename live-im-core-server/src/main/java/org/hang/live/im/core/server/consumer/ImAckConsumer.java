package org.hang.live.im.core.server.consumer;

import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.hang.live.common.interfaces.topic.ImCoreServerProviderTopicNames;
import org.hang.live.framework.mq.starter.properties.RocketMQConsumerProperties;
import org.hang.live.im.core.server.service.IMsgAckCheckService;
import org.hang.live.im.core.server.service.IRouterHandlerService;
import org.hang.live.im.dto.ImMsgBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

/**
 * MQ Consumer for resending unacked message.
 * @Author hang
 * @Date: Created in 22:54 2024/8/12
 * @Description
 */
@Configuration
public class ImAckConsumer implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImAckConsumer.class);

    @Resource
    private RocketMQConsumerProperties rocketMQConsumerProperties;
    @Resource
    private IMsgAckCheckService msgAckCheckService;
    @Resource
    private IRouterHandlerService routerHandlerService;

    @Override
    public void afterPropertiesSet() throws Exception {
        DefaultMQPushConsumer mqPushConsumer = new DefaultMQPushConsumer();

        mqPushConsumer.setVipChannelEnabled(false);
        mqPushConsumer.setNamesrvAddr(rocketMQConsumerProperties.getNameSrv());
        mqPushConsumer.setConsumerGroup(rocketMQConsumerProperties.getGroupName() + "_" + ImAckConsumer.class.getSimpleName());
        mqPushConsumer.setConsumeMessageBatchMaxSize(1);
        mqPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        mqPushConsumer.subscribe(ImCoreServerProviderTopicNames.QIYU_LIVE_IM_ACK_MSG_TOPIC, "");
        mqPushConsumer.setMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            //When the delayed message sends to this MQ, it checks if the message in Redis exists. If it does, resend the message.
            String json = new String(msgs.get(0).getBody());
            ImMsgBody imMsgBody = JSON.parseObject(json, ImMsgBody.class);
            //Get retry times for the message in Redis. -1 means no record found.
            int retryTimes = msgAckCheckService.getMsgAckTimes(imMsgBody.getMsgId(), imMsgBody.getUserId(), imMsgBody.getAppId());
            LOGGER.info("retryTimes is {},msgId is {}", retryTimes, imMsgBody.getMsgId());
            //No need to resend if clients removed record for this message in Redis.
            if (retryTimes < 0) {
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
            //Only support one times for resending.
            if (retryTimes < 2) {
                msgAckCheckService.recordMsgAck(imMsgBody, retryTimes + 1);
                msgAckCheckService.sendDelayMsg(imMsgBody);
                routerHandlerService.sendMsgToClient(imMsgBody);
            }
            //If retryTime is 2, this means resending is failed, discard the message...
            else {
                msgAckCheckService.doMsgAck(imMsgBody);
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        mqPushConsumer.start();
        LOGGER.info("mq consumer for resending unacked message is started,namesrv is {}", rocketMQConsumerProperties.getNameSrv());
    }


}
