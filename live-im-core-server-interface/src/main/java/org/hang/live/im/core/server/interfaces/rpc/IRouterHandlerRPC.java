package org.hang.live.im.core.server.interfaces.rpc;

import org.hang.live.im.core.server.interfaces.dto.ImMsgBody;

import java.util.List;

/**
 * The Dubbo interface for Routers to send message in batch.
 *
 * @Author hang
 * @Date: Created in 10:19 2024/8/14
 * @Description
 */
public interface IRouterHandlerRPC {

    /**
     * Send message based on user id.
     *
     * @param imMsgBody
     */
    void sendMsg(ImMsgBody imMsgBody);

    /**
     * Send batch message based on user id.
     *
     * @param imMsgBodyList
     */
    void batchSendMsg(List<ImMsgBody> imMsgBodyList);
}
