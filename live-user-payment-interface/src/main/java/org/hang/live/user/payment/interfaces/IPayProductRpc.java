package org.hang.live.user.payment.interfaces;

import org.hang.live.user.payment.dto.PayProductDTO;

import java.util.List;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
public interface IPayProductRpc {

    /**
     * Return a Bunch of Products of A Specific Business Type
     *
     * @param type Business Type (1 for live stream application)
     */
    List<PayProductDTO> products(Integer type);


    /**
     * Search Product by ID.
     *
     * @param productId
     * @return
     */
    PayProductDTO getByProductId(Integer productId);
}
