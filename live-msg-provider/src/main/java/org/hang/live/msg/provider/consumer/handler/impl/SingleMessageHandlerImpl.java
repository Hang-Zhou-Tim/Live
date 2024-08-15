package org.hang.live.msg.provider.consumer.handler.impl;

import com.alibaba.fastjson.JSON;
import org.apache.dubbo.config.annotation.DubboReference;
import org.hang.live.im.constants.AppIdEnum;
import org.hang.live.im.dto.ImMsgBody;
import org.hang.live.im.router.interfaces.constants.ImMsgBizCodeEnum;
import org.hang.live.im.router.interfaces.rpc.ImRouterRpc;
import org.hang.live.living.interfaces.dto.LivingRoomReqDTO;
import org.hang.live.living.interfaces.rpc.ILivingRoomRpc;
import org.hang.live.msg.dto.MessageDTO;
import org.hang.live.msg.provider.consumer.handler.MessageHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author hang
 * @Date: Created in 22:41 2024/8/14
 * @Description
 */
@Component
public class SingleMessageHandlerImpl implements MessageHandler {

    @DubboReference
    private ImRouterRpc routerRpc;
    @DubboReference
    private ILivingRoomRpc livingRoomRpc;

    // Broadcaster Batched Message for the Room
    @Override
    public void onMsgReceive(ImMsgBody imMsgBody) {
        int bizCode = imMsgBody.getBizCode();
        //Broadcasting Message in the room
        if (ImMsgBizCodeEnum.LIVING_ROOM_IM_CHAT_MSG_BIZ.getCode() == bizCode) {

            MessageDTO messageDTO = JSON.parseObject(imMsgBody.getData(), MessageDTO.class);
            Integer roomId = messageDTO.getRoomId();
            LivingRoomReqDTO reqDTO = new LivingRoomReqDTO();
            reqDTO.setRoomId(roomId);
            reqDTO.setAppId(imMsgBody.getAppId());
            //Exclude the sender
            List<Long> userIdList = livingRoomRpc.queryUserIdByRoomId(reqDTO).stream().filter(x->!x.equals(imMsgBody.getUserId())).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(userIdList)) {
                return;
            }
            List<ImMsgBody> imMsgBodies = new ArrayList<>();
            //Attach online users with the message to be sent.
            userIdList.forEach(userId -> {
                ImMsgBody respMsg = new ImMsgBody();
                respMsg.setUserId(userId);
                respMsg.setAppId(AppIdEnum.LIVE_BIZ.getCode());
                respMsg.setBizCode(ImMsgBizCodeEnum.LIVING_ROOM_IM_CHAT_MSG_BIZ.getCode());
                respMsg.setData(JSON.toJSONString(messageDTO));
                imMsgBodies.add(respMsg);
            });
            //Broadcaster all messages to router
            routerRpc.batchSendMsg(imMsgBodies);
        }
    }
}
