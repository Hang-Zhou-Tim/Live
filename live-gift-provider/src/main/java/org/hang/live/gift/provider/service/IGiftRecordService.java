package org.hang.live.gift.provider.service;

import org.hang.live.gift.dto.GiftRecordDTO;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
public interface IGiftRecordService {

    /**
     * Insert a new gift record.
     *
     * @param giftRecordDTO
     */
    void insertOne(GiftRecordDTO giftRecordDTO);

}
