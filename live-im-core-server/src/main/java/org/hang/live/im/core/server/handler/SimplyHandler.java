package org.hang.live.im.core.server.handler;

import io.netty.channel.ChannelHandlerContext;
import org.hang.live.im.core.server.common.ImMsg;

/**
 * @Author hang
 * @Date: Created in 20:39 2024/8/13
 * @Description
 */
public interface SimplyHandler {

    /**
     * Message Handler Function
     *
     * @param ctx
     * @param imMsg
     */
    void handle(ChannelHandlerContext ctx, ImMsg imMsg);
}
