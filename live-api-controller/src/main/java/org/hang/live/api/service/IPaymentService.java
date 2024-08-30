package org.hang.live.api.service;

import org.hang.live.api.vo.req.BuyCurrencyReqVO;
import org.hang.live.api.vo.resp.BuyCurrencyRespVO;
import org.hang.live.api.vo.resp.CurrencyVO;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/21
 * @Description
 */
public interface IPaymentService {

    /**
     * search all products of a specific business type
     *
     * @param type
     * @return
     */
    CurrencyVO getAllCurrencyAmounts(Integer type);

    /**
     * buy product
     *
     * @param buyCurrencyReqVO
     * @return
     */
    BuyCurrencyRespVO buyCurrency(BuyCurrencyReqVO buyCurrencyReqVO);
}
