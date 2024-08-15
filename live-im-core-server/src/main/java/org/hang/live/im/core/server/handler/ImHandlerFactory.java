package org.hang.live.im.core.server.handler;

import io.netty.channel.ChannelHandlerContext;
import org.hang.live.im.core.server.common.ImMsg;

/**
 * @Author hang
 * @Date: Created in 20:42 2024/8/13
 * @Description
 */
public interface ImHandlerFactory {

    /**
     * Based on code of message to choose handler.
     *
     * @param channelHandlerContext
     * @param imMsg
     */
    void doMsgHandler(ChannelHandlerContext channelHandlerContext, ImMsg imMsg);
}
