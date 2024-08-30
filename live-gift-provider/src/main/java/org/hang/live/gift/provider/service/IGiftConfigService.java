package org.hang.live.gift.provider.service;

import org.hang.live.gift.dto.GiftConfigDTO;

import java.util.List;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
public interface IGiftConfigService {

    /**
     * query based on gift id
     *
     * @param giftId
     * @return
     */
    GiftConfigDTO getByGiftId(Integer giftId);

    /**
     * query all gifts.(used in most situations)
     *
     * @return
     */
    List<GiftConfigDTO> queryGiftList();

    /**
     * insert a new gift configuration.
     *
     * @param giftConfigDTO
     */
    void insertOne(GiftConfigDTO giftConfigDTO);

    /**
     * update a gift configuration.
     *
     * @param giftConfigDTO
     */
    void updateOne(GiftConfigDTO giftConfigDTO);
}
