package org.hang.live.user.payment.interfaces;

import org.hang.live.user.payment.dto.TransactionTurnoverReqDTO;
import org.hang.live.user.payment.dto.TransactionTurnoverRespDTO;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
public interface IAccountBalanceRpc {

    /**
     * Increment Virtual Coint in User Account.
     *
     * @param userId
     * @param num
     */
    void incr(long userId,int num);

    /**
     * Decrement Virtual Coint in User Account.
     *
     * @param userId
     * @param num
     */
    void decr(long userId,int num);

    /**
     * Query Balance of User Account.
     *
     * @param userId
     * @return
     */
    Integer getBalance(long userId);


    /**
     * Method That Sending Gift Comsumer Used to Record Transaction Turnover.
     *
     * @param transactionTurnoverReqDTO
     */
    TransactionTurnoverRespDTO consumeForSendGift(TransactionTurnoverReqDTO transactionTurnoverReqDTO);


}
