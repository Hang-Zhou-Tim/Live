package org.hang.live.msg.provider.consumer.handler;

import org.hang.live.im.dto.ImMsgBody;

/**
 * @Author hang
 * @Date: Created in 22:40 2024/8/14
 * @Description
 */
public interface MessageHandler {

    /**
     * Deal with the message sent by client
     *
     * @param imMsgBody
     */
    void onMsgReceive(ImMsgBody imMsgBody);
}
