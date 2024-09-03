package org.hang.live.user.payment.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.hang.live.user.payment.dto.CurrencyDTO;
import org.hang.live.user.payment.interfaces.ICurrencyRPC;
import org.hang.live.user.payment.provider.service.ICurrencyService;

import java.util.List;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
@DubboService(timeout = 3000)
public class CurrencyRPCImpl implements ICurrencyRPC {

    @Resource
    private ICurrencyService payProductService;

    @Override
    public List<CurrencyDTO> getAllCurrencyAmounts(Integer type) {
        return payProductService.getAllCurrencyAmounts(type);
    }

    @Override
    public CurrencyDTO getByCurrencyId(Integer currencyId) {
        return payProductService.getByCurrencyId(currencyId);
    }
}
