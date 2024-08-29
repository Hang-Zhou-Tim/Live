package org.hang.live.im.core.server.service;

import org.hang.live.im.core.server.interfaces.dto.ImMsgBody;

/**
 * @Author hang
 * @Date: Created in 20:44 2024/8/13
 * @Description
 */
public interface IMsgAckCheckService {

    /**
     * When the client sends ack packet to server, remove record for the message in Redis.
     *
     * @param imMsgBody
     */
    void doMsgAck(ImMsgBody imMsgBody);

    /**
     * Record message ack and retry times in Redis
     *
     * @param imMsgBody
     * @param times
     */
    void recordMsgAck(ImMsgBody imMsgBody, int times);

    /**
     * Send delay message to know if it needs retry sending.
     *
     * @param imMsgBody
     */
    void sendDelayMsg(ImMsgBody imMsgBody);

    /**
     * Get resending times for a message sending record.
     *
     * @param msgId
     * @param userId
     * @param appId
     * @return
     */
    int getMsgAckTimes(String msgId,long userId,int appId);
}
