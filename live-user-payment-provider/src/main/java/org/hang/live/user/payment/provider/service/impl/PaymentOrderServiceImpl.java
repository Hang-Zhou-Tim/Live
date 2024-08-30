package org.hang.live.user.payment.provider.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.producer.MQProducer;
import org.hang.live.user.payment.constants.OrderStatusEnum;
import org.hang.live.user.payment.constants.PayProductTypeEnum;
import org.hang.live.user.payment.dto.PaymentOrderDTO;
import org.hang.live.user.payment.dto.CurrencyDTO;
import org.hang.live.user.payment.provider.dao.maper.IPaymentOrderMapper;
import org.hang.live.user.payment.provider.dao.po.PaymentOrderPO;
import org.hang.live.user.payment.provider.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
@Service
public class PaymentOrderServiceImpl implements IPaymentOrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentOrderServiceImpl.class);

    @Resource
    private IPaymentOrderMapper payOrderMapper;
    @Resource
    private ICurrencyService currencyService;
    @Resource
    private IPaymentTopicService payTopicService;
    @Resource
    private MQProducer mqProducer;
    @Resource
    private IAccountBalanceService accountBalanceService;
    @Resource
    private ITransactionTurnoverService transactionTurnoverService;

    @Override
    public PaymentOrderPO queryByOrderId(String orderId) {
        LambdaQueryWrapper<PaymentOrderPO> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(PaymentOrderPO::getOrderId, orderId);
        queryWrapper.last("limit 1");
        return payOrderMapper.selectOne(queryWrapper);
    }

    @Override
    public String insertOne(PaymentOrderPO paymentOrderPO) {
        String orderId = UUID.randomUUID().toString();
        paymentOrderPO.setOrderId(orderId);
        payOrderMapper.insert(paymentOrderPO);
        return orderId;
    }

    @Override
    public boolean updateOrderStatus(Long id, Integer status) {
        PaymentOrderPO paymentOrderPO = new PaymentOrderPO();
        paymentOrderPO.setId(id);
        paymentOrderPO.setStatus(status);
        payOrderMapper.updateById(paymentOrderPO);
        return true;
    }

    @Override
    public boolean updateOrderStatus(String orderId, Integer status) {
        PaymentOrderPO paymentOrderPO = new PaymentOrderPO();
        paymentOrderPO.setStatus(status);
        LambdaUpdateWrapper<PaymentOrderPO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(PaymentOrderPO::getOrderId, orderId);
        payOrderMapper.update(paymentOrderPO, updateWrapper);
        return true;
    }

    @Override
    public boolean paymentCallback(PaymentOrderDTO paymentOrderDTO) {
        PaymentOrderPO paymentOrderPO = this.queryByOrderId(paymentOrderDTO.getOrderId());
        if (paymentOrderPO == null) {
            LOGGER.error("error paymentOrderPO, paymentOrderDTO is {}", paymentOrderDTO);
            return false;
        }
        this.handleCallback(paymentOrderPO);
        //In this business context, we can also apply some other business logic here after payment finished.
        //For example, user-interest analyse.

        return true;
    }

    /**
     * Increase User Balance after Payment Finished.
     *
     * @param paymentOrderPO
     */
    private void handleCallback(PaymentOrderPO paymentOrderPO) {
        this.updateOrderStatus(paymentOrderPO.getOrderId(), OrderStatusEnum.PAYED.getCode());
        Integer currencyId = paymentOrderPO.getProductId();
        CurrencyDTO currencyDTO = currencyService.getByCurrencyId(currencyId);
        if (currencyDTO != null &&
                PayProductTypeEnum.COIN.getCode().equals(currencyDTO.getType())) {
            Long userId = paymentOrderPO.getUserId();
            JSONObject jsonObject = JSON.parseObject(currencyDTO.getExtra());
            Integer num = jsonObject.getInteger("coin");
            accountBalanceService.incrementBalance(userId,num);
        }
    }
}
