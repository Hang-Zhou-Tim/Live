package org.hang.live.im.core.server.handler.impl;

import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.hang.live.common.interfaces.topic.ImCoreServerProviderTopicNames;
import org.hang.live.im.core.server.common.ImContextUtils;
import org.hang.live.im.core.server.common.ImMsg;
import org.hang.live.im.core.server.handler.SimplyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Business Logic Handler
 * @Author hang
 * @Date: Created in 20:42 2024/8/12
 * @Description
 */
@Component
public class BizImMsgHandler implements SimplyHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BizImMsgHandler.class);

    @Resource
    private MQProducer mqProducer;

    @Override
    public void handle(ChannelHandlerContext ctx, ImMsg imMsg) {
        // Parameter Validation
        Long userId = ImContextUtils.getUserId(ctx);
        Integer appId = ImContextUtils.getAppId(ctx);
        if (userId == null || appId == null) {
            LOGGER.error("attr error,imMsg is {}", imMsg);
            // If there is no user id mapped with the channel, close the channel.
            ctx.close();
            throw new IllegalArgumentException("attr is error");
        }
        byte[] body = imMsg.getBody();
        if (body == null || body.length == 0) {
            LOGGER.error("body error,imMsg is {}", imMsg);
            return;
        }
        // Send Business Logic Activation Message to MQ.
        Message message = new Message();
        message.setTopic(ImCoreServerProviderTopicNames.QIYU_LIVE_IM_BIZ_MSG_TOPIC);
        message.setBody(body);
        try {
            SendResult sendResult = mqProducer.send(message);
            LOGGER.info("[BizImMsgHandler]Message Sending Results:{}", sendResult);
        } catch (Exception e) {
            LOGGER.error("Sending Error! Error is :", e);
            throw new RuntimeException(e);
        }
    }
}
