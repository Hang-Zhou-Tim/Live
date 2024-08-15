package org.hang.live.im.core.server.handler.impl;

import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import org.hang.live.im.constants.ImMsgCodeEnum;
import org.hang.live.im.core.server.common.ImMsg;
import org.hang.live.im.core.server.handler.ImHandlerFactory;
import org.hang.live.im.core.server.handler.SimplyHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author hang
 * @Date: Created in 20:42 2024/8/12
 * @Description
 */
@Component
public class ImHandlerFactoryImpl implements ImHandlerFactory, InitializingBean {

    private static final Map<Integer, SimplyHandler> simplyHandlerMap = new HashMap<>();
    @Resource
    private ApplicationContext applicationContext;

    @Override
    public void doMsgHandler(ChannelHandlerContext channelHandlerContext, ImMsg imMsg) {
        SimplyHandler simplyHandler = simplyHandlerMap.get(imMsg.getCode());
        if (simplyHandler == null) {
            throw new IllegalArgumentException("msg code is error,code is :" + imMsg.getCode());
        }
        simplyHandler.handle(channelHandlerContext, imMsg);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //1. Get Bean of Login Message Handler: Validate IM token，and map channel with user id
        //2. Get Bean of Logout Message Handler: Remove pairs of channel and user id, also notify MQ message.
        //3. Get Bean of Business Message Handler: Dealing with business logic.
        //4. Get Bean of Heart Beats Handler: Control the heart beats;
        simplyHandlerMap.put(ImMsgCodeEnum.IM_LOGIN_MSG.getCode(),applicationContext.getBean(LoginMsgHandler.class));
        simplyHandlerMap.put(ImMsgCodeEnum.IM_LOGOUT_MSG.getCode(), applicationContext.getBean(LogoutMsgHandler.class));
        simplyHandlerMap.put(ImMsgCodeEnum.IM_BIZ_MSG.getCode(), applicationContext.getBean(BizImMsgHandler.class));
        simplyHandlerMap.put(ImMsgCodeEnum.IM_HEARTBEAT_MSG.getCode(), applicationContext.getBean(HeartBeatImMsgHandler.class));
        simplyHandlerMap.put(ImMsgCodeEnum.IM_ACK_MSG.getCode(), applicationContext.getBean(AckImMsgHandler.class));
    }
}
