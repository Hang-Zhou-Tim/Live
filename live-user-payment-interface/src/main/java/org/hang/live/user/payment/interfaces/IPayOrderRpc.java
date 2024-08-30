package org.hang.live.user.payment.interfaces;


import org.hang.live.user.payment.dto.PayOrderDTO;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
public interface IPayOrderRpc {

    /**
     * Insert a New Payment Order
     *
     * @param payOrderDTO
     */
    String insertOne(PayOrderDTO payOrderDTO);


    /**
     * Update Status of Order by Primary ID
     *
     * @param id
     * @param status
     */
    boolean updateOrderStatus(Long id,Integer status);

    /**
     * Update Status of Order by Primary ID
     *
     * @param orderId
     * @param status
     */
    boolean updateOrderStatus(String orderId,Integer status);


    /**
     * Deal with Payment Callback
     *
     * @param payOrderDTO
     * @return
     */
    boolean payNotify(PayOrderDTO payOrderDTO);
}
