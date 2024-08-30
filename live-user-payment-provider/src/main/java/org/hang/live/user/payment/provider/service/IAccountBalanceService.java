package org.hang.live.user.payment.provider.service;

import org.hang.live.user.payment.dto.TransactionTurnoverReqDTO;
import org.hang.live.user.payment.dto.TransactionTurnoverRespDTO;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
public interface IAccountBalanceService {

    /**
     * Insert a new user
     *
     * @param userId
     */
    boolean insertOne(long userId);

    /**
     * increment virtual coin
     *
     * @param userId
     * @param num
     */
    void incr(long userId,int num);

    /**
     * decrement virtual coin
     *
     * @param userId
     * @param num
     */
    void decr(long userId,int num);

    /**
     * query balance
     *
     * @param userId
     * @return
     */
    Integer getBalance(long userId);

    /**
     * method used by sending gift consumers to decrease user balance and record this transaction turnover.
     * @param transactionTurnoverReqDTO
     */
    TransactionTurnoverRespDTO consumeForSendGift(TransactionTurnoverReqDTO transactionTurnoverReqDTO);
}
