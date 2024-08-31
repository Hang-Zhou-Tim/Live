package org.hang.live.im.server.router.interfaces.rpc;
import org.hang.live.im.core.server.interfaces.dto.ImMsgBody;

import java.util.List;

/**
 * @Author hang
 * @Date: Created in 16:21 2024/8/12
 * @Description
 */
public interface ImRouterRPC {


    /**
     * send message
     *
     * @param imMsgBody
     * @return
     */
    boolean sendMsg(ImMsgBody imMsgBody);


    /**
     * Send message in batch
     *
     * @param imMsgBody
     */
    void batchSendMsg(List<ImMsgBody> imMsgBody);
}
