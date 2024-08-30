package org.hang.live.user.payment.provider.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.producer.MQProducer;
import org.hang.live.user.payment.constants.OrderStatusEnum;
import org.hang.live.user.payment.constants.PayProductTypeEnum;
import org.hang.live.user.payment.dto.PayOrderDTO;
import org.hang.live.user.payment.dto.PayProductDTO;
import org.hang.live.user.payment.provider.dao.maper.IPayOrderMapper;
import org.hang.live.user.payment.provider.dao.po.PayOrderPO;
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
public class PayOrderServiceImpl implements IPayOrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PayOrderServiceImpl.class);

    @Resource
    private IPayOrderMapper payOrderMapper;
    @Resource
    private IPayProductService payProductService;
    @Resource
    private IPayTopicService payTopicService;
    @Resource
    private MQProducer mqProducer;
    @Resource
    private IAccountBalanceService accountBalanceService;
    @Resource
    private ITransactionTurnoverService transactionTurnoverService;

    @Override
    public PayOrderPO queryByOrderId(String orderId) {
        LambdaQueryWrapper<PayOrderPO> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(PayOrderPO::getOrderId, orderId);
        queryWrapper.last("limit 1");
        return payOrderMapper.selectOne(queryWrapper);
    }

    @Override
    public String insertOne(PayOrderPO payOrderPO) {
        String orderId = UUID.randomUUID().toString();
        payOrderPO.setOrderId(orderId);
        payOrderMapper.insert(payOrderPO);
        return orderId;
    }

    @Override
    public boolean updateOrderStatus(Long id, Integer status) {
        PayOrderPO payOrderPO = new PayOrderPO();
        payOrderPO.setId(id);
        payOrderPO.setStatus(status);
        payOrderMapper.updateById(payOrderPO);
        return true;
    }

    @Override
    public boolean updateOrderStatus(String orderId, Integer status) {
        PayOrderPO payOrderPO = new PayOrderPO();
        payOrderPO.setStatus(status);
        LambdaUpdateWrapper<PayOrderPO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(PayOrderPO::getOrderId, orderId);
        payOrderMapper.update(payOrderPO, updateWrapper);
        return true;
    }

    @Override
    public boolean payNotify(PayOrderDTO payOrderDTO) {
        PayOrderPO payOrderPO = this.queryByOrderId(payOrderDTO.getOrderId());
        if (payOrderPO == null) {
            LOGGER.error("error payOrderPO, payOrderDTO is {}", payOrderDTO);
            return false;
        }
        this.payNotifyHandler(payOrderPO);
        //In this business context, we can also apply some other business logic here after payment finished.
        //For example, user-interest analyse.

        return true;
    }

    /**
     * Increase User Balance after Payment Finished.
     *
     * @param payOrderPO
     */
    private void payNotifyHandler(PayOrderPO payOrderPO) {
        this.updateOrderStatus(payOrderPO.getOrderId(), OrderStatusEnum.PAYED.getCode());
        Integer productId = payOrderPO.getProductId();
        PayProductDTO payProductDTO = payProductService.getByProductId(productId);
        if (payProductDTO != null &&
                PayProductTypeEnum.COIN.getCode().equals(payProductDTO.getType())) {
            Long userId = payOrderPO.getUserId();
            JSONObject jsonObject = JSON.parseObject(payProductDTO.getExtra());
            Integer num = jsonObject.getInteger("coin");
            accountBalanceService.incr(userId,num);
        }
    }
}
