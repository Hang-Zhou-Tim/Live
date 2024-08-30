package org.hang.live.user.payment.provider.service.impl;

import jakarta.annotation.Resource;
import org.hang.live.user.payment.provider.dao.maper.IAccountBalanceMapper;
import org.hang.live.user.payment.provider.dao.po.AccountBalancePO;
import org.hang.live.user.payment.provider.service.IAccountBalanceService;
import org.hang.live.user.payment.provider.service.ITransactionTurnoverService;
import org.hang.live.common.redis.configuration.key.BankProviderCacheKeyBuilder;
import org.hang.live.user.payment.constants.PaymentTypeEnum;
import org.hang.live.user.payment.dto.TransactionTurnoverReqDTO;
import org.hang.live.user.payment.dto.TransactionTurnoverRespDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
@Service
public class AccountBalanceServiceImpl implements IAccountBalanceService {

    Logger logger = LoggerFactory.getLogger(AccountBalanceServiceImpl.class);
    @Resource
    private IAccountBalanceMapper accountBalanceMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private BankProviderCacheKeyBuilder cacheKeyBuilder;
    @Resource
    private ITransactionTurnoverService transactionTurnoverService;

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 4, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000));

    @Override
    public boolean insertOne(long userId) {
        try {
            AccountBalancePO accountPO = new AccountBalancePO();
            accountPO.setUserId(userId);
            accountBalanceMapper.insert(accountPO);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    @Override
    public void incr(long userId, int num) {
        String cacheKey = cacheKeyBuilder.buildUserBalance(userId);
        if (redisTemplate.hasKey(cacheKey)) {
            redisTemplate.opsForValue().increment(cacheKey, num);
            redisTemplate.expire(cacheKey, 5, TimeUnit.MINUTES);
        }
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                //Increase Balance of User Account to DB
                consumeIncrDBHandler(userId, num);
            }
        });
    }

    @Override
    public void decr(long userId, int num) {
        String cacheKey = cacheKeyBuilder.buildUserBalance(userId);
        if (redisTemplate.hasKey(cacheKey)) {
            redisTemplate.opsForValue().decrement(cacheKey, num);
            redisTemplate.expire(cacheKey, 5, TimeUnit.MINUTES);
        }
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                //Decrement Balance of User Account to DB
                consumeDecrDBHandler(userId, num);
            }
        });
    }

    @Override
    public Integer getBalance(long userId) {
        String cacheKey = cacheKeyBuilder.buildUserBalance(userId);
        Object cacheBalance = redisTemplate.opsForValue().get(cacheKey);
        if (cacheBalance != null) {
            if ((Integer) cacheBalance == -1) {
                return null;
            }
            return (Integer) cacheBalance;
        }
        Integer currentBalance = accountBalanceMapper.queryBalance(userId);
        if (currentBalance == null) {
            redisTemplate.opsForValue().set(cacheKey, -1, 5, TimeUnit.MINUTES);
            return null;
        }
        redisTemplate.opsForValue().set(cacheKey, currentBalance, 30, TimeUnit.MINUTES);
        return currentBalance;
    }

    @Override
    public TransactionTurnoverRespDTO consumeForSendGift(TransactionTurnoverReqDTO transactionTurnoverReqDTO) {
        //Remaining Balance.
        long userId = transactionTurnoverReqDTO.getUserId();
        int num = transactionTurnoverReqDTO.getNum();
        Integer balance = this.getBalance(userId);
        if (balance == null || balance < num) {
            return TransactionTurnoverRespDTO.buildFail(userId, "Balance Deduction Failed", 1);
        }
        this.decr(userId, num);
        return TransactionTurnoverRespDTO.buildSuccess(userId, "Balance Deduction Succeeds");
    }

    @Transactional(rollbackFor = Exception.class)
    public void consumeIncrDBHandler(long userId, int num) {
        //Get balance with use id
        boolean res = accountBalanceMapper.selectById(userId) == null;

        if(res){
            AccountBalancePO accountBalancePO = new AccountBalancePO();
            accountBalancePO.setUserId(userId);
            accountBalancePO.setCurrentBalance(num);
            //Increment the amount of coin into account in DB.
            accountBalanceMapper.insert(accountBalancePO);
        }else{
            res = accountBalanceMapper.incr(userId, num);
            logger.info("Increment {} amount of money to user {}, res is {}", num, userId, res);
        }
        //Insert Turnover.
        transactionTurnoverService.insertOne(userId, num, PaymentTypeEnum.SEND_GIFT.getCode());
    }

    @Transactional(rollbackFor = Exception.class)
    public void consumeDecrDBHandler(long userId, int num) {
        //Get balance with use id
        boolean res = accountBalanceMapper.selectById(userId) != null;
        if(!res) {
            return;
        }

        //Decrement account balance in DB.
        res = accountBalanceMapper.decr(userId, num);
        logger.info("Decrease {} amount of money to user {}, res is {}",num,userId, res);
        //Insert Turnover.
        transactionTurnoverService.insertOne(userId, num * -1, PaymentTypeEnum.SEND_GIFT.getCode());
    }
}
