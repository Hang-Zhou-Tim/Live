package org.hang.live.user.payment.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.hang.live.user.payment.dto.TransactionTurnoverReqDTO;
import org.hang.live.user.payment.dto.TransactionTurnoverRespDTO;
import org.hang.live.user.payment.interfaces.IAccountBalanceRPC;
import org.hang.live.user.payment.provider.service.IAccountBalanceService;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
@DubboService
public class AccountBalanceRpcImpl implements IAccountBalanceRPC {

    @Resource
    private IAccountBalanceService AccountBalanceService;

    @Override
    public void incrementBalance(long userId, int num) {
        AccountBalanceService.incrementBalance(userId, num);
    }

    @Override
    public void decrementBalance(long userId, int num) {
        AccountBalanceService.decrementBalance(userId, num);
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
