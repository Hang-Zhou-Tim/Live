package org.hang.live.im.core.server.handler.impl;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.hang.live.common.interfaces.topic.ImCoreServerProviderTopicNames;
import org.hang.live.im.constants.ImMsgCodeEnum;
import org.hang.live.im.core.server.common.ChannelHandlerContextCache;
import org.hang.live.im.core.server.common.ImContextUtils;
import org.hang.live.im.core.server.common.ImMsg;
import org.hang.live.im.core.server.handler.SimplyHandler;
import org.hang.live.im.core.server.interfaces.constants.ImCoreServerConstants;
import org.hang.live.im.core.server.interfaces.dto.ImOfflineDTO;
import org.hang.live.im.dto.ImMsgBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Logout Handler to deal with logout/inactive operation.
 *
 * @Author hang
 * @Date: Created in 20:40 2024/8/12
 * @Description
 */
@Component
public class LogoutMsgHandler implements SimplyHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogoutMsgHandler.class);

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private MQProducer mqProducer;

    @Override
    public void handle(ChannelHandlerContext ctx, ImMsg imMsg) {
        Long userId = ImContextUtils.getUserId(ctx);
        Integer appId = ImContextUtils.getAppId(ctx);
        if (userId == null || appId == null) {
            LOGGER.error("attr error,imMsg is {}", imMsg);
            //If there is no userId for this channel context, meaning it is never log in, close the ctx.
            ctx.close();
            throw new IllegalArgumentException("attr is error");
        }
        //send the message back for notifying logout.
        logoutMsgNotice(ctx,userId,appId);
        logoutHandler(ctx, userId, appId);
    }

    /**
     * Return ACK for logout
     *
     * @param ctx
     * @param userId
     * @param appId
     */
    private void logoutMsgNotice(ChannelHandlerContext ctx, Long userId, Integer appId) {
        ImMsgBody respBody = new ImMsgBody();
        respBody.setAppId(appId);
        respBody.setUserId(userId);
        respBody.setData("true");
        ImMsg respMsg = ImMsg.build(ImMsgCodeEnum.IM_LOGOUT_MSG.getCode(), JSON.toJSONString(respBody));
        ctx.writeAndFlush(respMsg);
        ctx.close();
    }

    /**
     * Remove pairs of user id and server socket channel in the context and send logout MQ message.
     *
     * @param ctx
     * @param userId
     * @param appId
     */
    public void logoutHandler(ChannelHandlerContext ctx, Long userId, Integer appId) {
        LOGGER.info("[LogoutMsgHandler] logout success,userId is {},appId is {}", userId, appId);
        //When user disconnects, send logout message.
        ChannelHandlerContextCache.remove(userId);
        stringRedisTemplate.delete(ImCoreServerConstants.IM_BIND_IP_KEY + appId + ":" + userId);
        ImContextUtils.removeUserId(ctx);
        ImContextUtils.removeAppId(ctx);
        sendLogoutMQ(ctx, userId, appId);
    }

    /**
     * Send Logout MQ message.
     *
     * @param ctx
     * @param userId
     * @param appId
     */
    public void sendLogoutMQ(ChannelHandlerContext ctx, Long userId, Integer appId) {
        ImOfflineDTO imOfflineDTO = new ImOfflineDTO();
        imOfflineDTO.setUserId(userId);
        imOfflineDTO.setRoomId(ImContextUtils.getRoomId(ctx));
        imOfflineDTO.setAppId(appId);
        imOfflineDTO.setLoginTime(System.currentTimeMillis());
        Message message = new Message();
        message.setTopic(ImCoreServerProviderTopicNames.IM_OFFLINE_TOPIC);
        message.setBody(JSON.toJSONString(imOfflineDTO).getBytes());
        try {
            SendResult sendResult = mqProducer.send(message);
            LOGGER.error("[sendLogoutMQ] result is {}", sendResult);
        } catch (Exception e) {
            LOGGER.error("[sendLogoutMQ] error is: ", e);
        }
    }
}
