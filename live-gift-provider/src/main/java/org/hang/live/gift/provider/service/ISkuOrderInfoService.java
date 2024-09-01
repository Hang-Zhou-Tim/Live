package org.hang.live.gift.provider.service;

import org.hang.live.gift.dto.SkuOrderInfoReqDTO;
import org.hang.live.gift.dto.SkuOrderInfoRespDTO;
import org.hang.live.gift.provider.dao.po.SkuOrderInfoPO;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/30
 * @Description
 */
public interface ISkuOrderInfoService {

    /**
     * query order info by userId and roomId
     */
    SkuOrderInfoRespDTO queryByUserIdAndRoomId(Long userId, Integer roomId);

    /**
     * insert a new order
     */
    SkuOrderInfoPO insertOne(SkuOrderInfoReqDTO skuOrderInfoReqDTO);

    /**
     * update order status
     */
    boolean updateOrderStatus(SkuOrderInfoReqDTO skuOrderInfoReqDTO);

    SkuOrderInfoRespDTO queryByOrderId(Long orderId);
}