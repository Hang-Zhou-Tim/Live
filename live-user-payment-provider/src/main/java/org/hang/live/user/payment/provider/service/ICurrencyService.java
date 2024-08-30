package org.hang.live.user.payment.provider.service;

import org.hang.live.user.payment.dto.CurrencyDTO;

import java.util.List;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
public interface ICurrencyService {

    /**
     * Return a batch of currency amount in database.
     *
     * @param type business type
     */
    List<CurrencyDTO> getAllCurrencyAmounts(Integer type);

    /**
     * Query product based on id.
     *
     * @param currencyId
     * @return
     */
    CurrencyDTO getByCurrencyId(Integer currencyId);
}
