package org.hang.live.user.payment.provider.service;

import org.hang.live.user.payment.dto.PayOrderDTO;
import org.hang.live.user.payment.provider.dao.po.PayOrderPO;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
public interface IPayOrderService {


    /**
     * query based on order id
     *
     * @param orderId
     */
    PayOrderPO queryByOrderId(String orderId);

    /**
     * insert payment order
     *
     * @param payOrderPO
     */
    String insertOne(PayOrderPO payOrderPO);


    /**
     * update order status by order id.
     *
     * @param id
     * @param status
     */
    boolean updateOrderStatus(Long id,Integer status);

    /**
     * update order status by order id.
     *
     * @param orderId
     * @param status
     */
    boolean updateOrderStatus(String orderId,Integer status);

    /**
     * Used when used finished payment in third party and triggered callbacks
     *
     * @param payOrderDTO
     * @return
     */
    boolean payNotify(PayOrderDTO payOrderDTO);
}
