package org.hang.live.user.payment.provider.service.impl;

import jakarta.annotation.Resource;
import org.hang.live.user.payment.provider.dao.maper.ITransactionTurnoverMapper;
import org.hang.live.user.payment.provider.service.ITransactionTurnoverService;
import org.hang.live.user.payment.provider.dao.po.TransactionTurnoverPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
@Service
public class TransactionTurnoverServiceImpl implements ITransactionTurnoverService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionTurnoverServiceImpl.class);

    @Resource
    private ITransactionTurnoverMapper transactionTurnoverMapper;

    @Override
    public boolean insertOne(long userId, int num, int type) {
        try {
            TransactionTurnoverPO tradePO = new TransactionTurnoverPO();
            tradePO.setUserId(userId);
            tradePO.setNum(num);
            tradePO.setType(type);
            transactionTurnoverMapper.insert(tradePO);
            return true;
        } catch (Exception e) {
            LOGGER.error("[TransactionTurnoverServiceImpl] insert error is:", e);
        }
        return false;
    }
}
