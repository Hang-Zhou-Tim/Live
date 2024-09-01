package org.hang.live.gift.interfaces;

import org.hang.live.gift.dto.PrepareOrderReqDTO;
import org.hang.live.gift.dto.SkuOrderInfoReqDTO;
import org.hang.live.gift.dto.SkuOrderInfoRespDTO;
import org.hang.live.gift.dto.SkuPrepareOrderInfoDTO;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/30
 * @Description
 */
public interface ISkuOrderInfoRPC {

    /**
     * query pre-order of the user by user id and room id
     */
    SkuOrderInfoRespDTO queryByUserIdAndRoomId(Long userId, Integer roomId);

    /**
     * insert a new order
     */
    boolean insertOne(SkuOrderInfoReqDTO skuOrderInfoReqDTO);

    /**
     * update order status
     */
    boolean updateOrderStatus(SkuOrderInfoReqDTO skuOrderInfoReqDTO);

    /**
     * pre-order generation
     */
    SkuPrepareOrderInfoDTO prepareOrder(PrepareOrderReqDTO reqDTO);

    /**
     * pay the order.
     */
    boolean payNow(Long userId, Integer roomId);
}