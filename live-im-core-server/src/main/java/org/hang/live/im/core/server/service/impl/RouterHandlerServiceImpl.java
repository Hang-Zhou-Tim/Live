package org.hang.live.im.core.server.service.impl;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import org.hang.live.im.core.server.interfaces.constants.ImMsgCodeEnum;
import org.hang.live.im.core.server.common.ChannelHandlerContextCache;
import org.hang.live.im.core.server.common.ImMsg;
import org.hang.live.im.core.server.service.IMsgAckCheckService;
import org.hang.live.im.core.server.service.IRouterHandlerService;
import org.hang.live.im.core.server.interfaces.dto.ImMsgBody;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @Author idea
 * @Date: Created in 22:35 2024/8/13
 * @Description
 */
@Service
public class RouterHandlerServiceImpl implements IRouterHandlerService {

    @Resource
    private IMsgAckCheckService msgAckCheckService;

    @Override
    public void onReceive(ImMsgBody imMsgBody) {
        //Send message to user based on their id.
        if(sendMsgToClient(imMsgBody)) {
            //message is sending to client, record this sending message and sending times.
            msgAckCheckService.recordMsgAck(imMsgBody, 1);
            msgAckCheckService.sendDelayMsg(imMsgBody);
        }
    }
    //Find the channel that correlated to this user in this server and send it to that channel.
    @Override
    public boolean sendMsgToClient(ImMsgBody imMsgBody) {
        Long userId = imMsgBody.getUserId();
        ChannelHandlerContext ctx = ChannelHandlerContextCache.get(userId);
        if (ctx != null) {
            String msgId = UUID.randomUUID().toString();
            imMsgBody.setMsgId(msgId);
            ImMsg respMsg = ImMsg.build(ImMsgCodeEnum.IM_BIZ_MSG.getCode(), JSON.toJSONString(imMsgBody));
            ctx.writeAndFlush(respMsg);
            return true;
        }
        return false;
    }
}
