package org.hang.live.user.payment.provider.service;

import org.hang.live.user.payment.dto.PayProductDTO;

import java.util.List;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
public interface IPayProductService {

    /**
     * Return a batch of products in database.
     *
     * @param type business type
     */
    List<PayProductDTO> products(Integer type);

    /**
     * Query product based on id.
     *
     * @param productId
     * @return
     */
    PayProductDTO getByProductId(Integer productId);
}
