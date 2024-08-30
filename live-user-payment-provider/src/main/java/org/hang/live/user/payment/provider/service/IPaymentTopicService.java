package org.hang.live.user.payment.provider.service;

import org.hang.live.user.payment.provider.dao.po.PaymentTopicPO;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
public interface IPaymentTopicService {

    /**
     * Query payment topic based on topic code.
     *
     * @param code
     * @return
     */
    PaymentTopicPO getByCode(Integer code);
}
