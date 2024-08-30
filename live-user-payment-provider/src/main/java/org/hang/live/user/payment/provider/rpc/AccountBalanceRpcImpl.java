package org.hang.live.user.payment.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.hang.live.user.payment.dto.TransactionTurnoverReqDTO;
import org.hang.live.user.payment.dto.TransactionTurnoverRespDTO;
import org.hang.live.user.payment.interfaces.IAccountBalanceRpc;
import org.hang.live.user.payment.provider.service.IAccountBalanceService;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
@DubboService
public class AccountBalanceRpcImpl implements IAccountBalanceRpc {

    @Resource
    private IAccountBalanceService AccountBalanceService;

    @Override
    public void incr(long userId, int num) {
        AccountBalanceService.incr(userId, num);
    }

    @Override
    public void decr(long userId, int num) {
        AccountBalanceService.decr(userId, num);
    }

    @Override
    public Integer getBalance(long userId) {
        return AccountBalanceService.getBalance(userId);
    }

    @Override
    public TransactionTurnoverRespDTO consumeForSendGift(TransactionTurnoverReqDTO transactionTurnoverReqDTO) {
        return AccountBalanceService.consumeForSendGift(transactionTurnoverReqDTO);
    }

}
