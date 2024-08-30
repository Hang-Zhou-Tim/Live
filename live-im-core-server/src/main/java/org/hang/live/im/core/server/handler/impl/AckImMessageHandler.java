package org.hang.live.im.core.server.handler.impl;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import org.hang.live.im.core.server.common.ImContextUtils;
import org.hang.live.im.core.server.common.ImMsg;
import org.hang.live.im.core.server.handler.SimplyHandler;
import org.hang.live.im.core.server.service.IMsgAckCheckService;
import org.hang.live.im.core.server.interfaces.dto.ImMsgBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Handler that sends back ACK to clients
 *
 * @Author hang
 * @Date: Created in 20:40 2024/8/12
 * @Description
 */
@Component
public class AckImMessageHandler implements SimplyHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AckImMessageHandler.class);

    @Resource
    private IMsgAckCheckService msgAckCheckService;

    @Override
    public void handle(ChannelHandlerContext ctx, ImMsg imMsg) {
        //Get user id and app id when this channel gets ACK message from client.
        Long userId = ImContextUtils.getUserId(ctx);
        Integer appid = ImContextUtils.getAppId(ctx);
        if (userId == null && appid == null) {
            ctx.close();
            throw new IllegalArgumentException("attr is error");
        }
        //Remove redis logs when clients already receives messages.
        msgAckCheckService.doMsgAck(JSON.parseObject(imMsg.getBody(), ImMsgBody.class));
    }
}
