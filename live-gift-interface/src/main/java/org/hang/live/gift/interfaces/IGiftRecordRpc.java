package org.hang.live.gift.interfaces;

import org.hang.live.gift.dto.GiftRecordDTO;


/**
 *
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description Sending Gift Record RPC
 */
public interface IGiftRecordRpc {

    /**
     * Insert a Gift by GiftRecordDTO
     *
     * @param giftRecordDTO
     */
    void insertOne(GiftRecordDTO giftRecordDTO);

}
