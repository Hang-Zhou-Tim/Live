package org.hang.live.user.payment.provider.service;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
public interface ITransactionTurnoverService {

    /**
     * Insert new transaction turnover record in database.
     *
     * @param userId
     * @param num
     * @param type
     * @return
     */
    boolean insertOne(long userId,int num,int type);
}
