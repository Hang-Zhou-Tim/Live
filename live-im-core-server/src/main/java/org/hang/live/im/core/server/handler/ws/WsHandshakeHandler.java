package org.hang.live.im.core.server.handler.ws;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.hang.live.im.core.server.handler.impl.LoginMessageHandler;
import org.hang.live.im.core.server.interfaces.rpc.ImTokenRPC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * ws handshake(connection and disconnect) handler
 *
 * @Author hang
 * @Date created in 9:30 2024/8/13
 */
@Component
@ChannelHandler.Sharable
public class WsHandshakeHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(WsHandshakeHandler.class);

    @Value("${hang.im.ws.port}")
    private int port;
    private String serverIp;
    @DubboReference
    private ImTokenRPC imTokenRpc;
    @Resource
    private LoginMessageHandler loginMsgHandler;

    private WebSocketServerHandshaker webSocketServerHandshaker;
    private static Logger logger = LoggerFactory.getLogger(WsHandshakeHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //Handshake for http upgrade packet.
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, ((FullHttpRequest) msg));
            return;
        }

        //Close Websocket connection.
        if (msg instanceof CloseWebSocketFrame) {
            webSocketServerHandshaker.close(ctx.channel(), (CloseWebSocketFrame) ((WebSocketFrame) msg).retain());
            return;
        }
        //Send message to next handler.
        ctx.fireChannelRead(msg);
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest msg) {
        try {
            serverIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        // ws://127.0.0.1:9091/{token%appId}/{userId}/{code}/{param like roomId}
        // web socket url.
        String webSocketUrl = "ws://" + serverIp + ":" + port;
        String uri = msg.uri();
        String[] paramArr = uri.split("/");
        String token = paramArr[1];
        Long userId = Long.valueOf(paramArr[2]);
        Long queryUserId = imTokenRpc.getUserIdByToken(token);
        LOGGER.info("Websocket is {} ", webSocketUrl);
        // The end substring is App Id.
        Integer appId = Integer.valueOf(token.substring(token.lastIndexOf("%") + 1));
        if (queryUserId == null || !queryUserId.equals(userId)) {
            LOGGER.error("[WsSharkHandler] token validation failed！");
            //Validation Failed
            ctx.close();
            return;
        }
        // build handshaker for websocket request, validate ws connection format.
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(webSocketUrl, null, false);
        webSocketServerHandshaker = wsFactory.newHandshaker(msg);
        // send unsupported message if connection failed
        if (webSocketServerHandshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            return;
        }
        //Starts Handshake
        ChannelFuture channelFuture = webSocketServerHandshaker.handshake(ctx.channel(), msg);
        //If the handshake succeeds, process Login operation.
        if (channelFuture.isSuccess()) {
            Integer code = Integer.valueOf(paramArr[3]);
            Integer roomId = null;
            //If this connection wants to enter room, return room id.
            if (code == ParamCodeEnum.LIVING_ROOM_LOGIN.getCode()) {
                roomId = Integer.valueOf(paramArr[4]);
            }
            //Handle the login process.
            loginMsgHandler.loginSuccessHandler(ctx, userId, appId, roomId);
            logger.info("[WebsocketSharkHandler] channel is connect!");
        }
    }

    enum ParamCodeEnum {
        LIVING_ROOM_LOGIN(1001, "Login Stream Live Room");

        int code;
        String desc;

        ParamCodeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }
}
