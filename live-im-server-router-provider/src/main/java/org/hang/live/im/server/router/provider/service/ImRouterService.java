package org.hang.live.im.server.router.provider.service;

import org.hang.live.im.core.server.interfaces.dto.ImMsgBody;

import java.util.List;

/**
 * @Author hang
 * @Date: Created in 10:30 2024/8/13
 * @Description
 */
public interface ImRouterService {


    /**
     * Send Message
     *
     * @param imMsgBody
     * @return
     */
    boolean sendMsg(ImMsgBody imMsgBody);

    /**
     * Send batch message
     *
     * @param imMsgBody
     */
    void batchSendMsg(List<ImMsgBody> imMsgBody);
}
