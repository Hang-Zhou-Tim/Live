package org.hang.live.gift.interfaces;

import org.hang.live.gift.dto.GiftConfigDTO;

import java.util.List;

/**
 *
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description CRUD Gift Configuration Record RPC
 */
public interface IGiftConfigRpc {

    /**
     * Query Gift Config by Gift Id
     *
     * @param giftId
     * @return
     */
    GiftConfigDTO getByGiftId(Integer giftId);

    /**
     * Query All Gifts.
     *
     * @return
     */
    List<GiftConfigDTO> queryGiftList();

    /**
     * Insert a New Gift Config
     *
     * @param giftConfigDTO
     */
    void insertOne(GiftConfigDTO giftConfigDTO);

    /**
     * Update a Gift Configuration
     *
     * @param giftConfigDTO
     */
    void updateOne(GiftConfigDTO giftConfigDTO);
}
