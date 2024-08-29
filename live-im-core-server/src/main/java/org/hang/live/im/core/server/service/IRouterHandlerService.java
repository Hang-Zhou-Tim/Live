package org.hang.live.im.core.server.service;

import org.hang.live.im.core.server.interfaces.dto.ImMsgBody;

/**
 * @Author idea
 * @Date: Created in 22:31 2024/8/13
 * @Description
 */
public interface IRouterHandlerService {

    /**
     * When received RPC calls to send message to specific users, use this method.
     *
     * @param imMsgBody
     */
    void onReceive(ImMsgBody imMsgBody);


    /**
     * 发送消息给客户端
     *
     * @param imMsgBody
     */
    boolean sendMsgToClient(ImMsgBody imMsgBody);
}
