package org.hang.live.api.service;

import org.hang.live.api.vo.req.PayProductReqVO;
import org.hang.live.api.vo.resp.PayProductRespVO;
import org.hang.live.api.vo.resp.PayProductVO;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/21
 * @Description
 */
public interface IBankService {

    /**
     * search all products of a specific business type
     *
     * @param type
     * @return
     */
    PayProductVO products(Integer type);

    /**
     * buy product
     *
     * @param payProductReqVO
     * @return
     */
    PayProductRespVO payProduct(PayProductReqVO payProductReqVO);
}
