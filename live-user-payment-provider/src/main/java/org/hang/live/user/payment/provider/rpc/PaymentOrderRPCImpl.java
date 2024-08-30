package org.hang.live.user.payment.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.hang.live.user.payment.dto.PaymentOrderDTO;
import org.hang.live.user.payment.interfaces.IPaymentOrderRPC;
import org.hang.live.user.payment.provider.dao.po.PaymentOrderPO;
import org.hang.live.user.payment.provider.service.IPaymentOrderService;
import org.hang.live.common.interfaces.utils.ConvertBeanUtils;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
@DubboService
public class PaymentOrderRPCImpl implements IPaymentOrderRPC {

    @Resource
    private IPaymentOrderService paymentOrderService;

    @Override
    public String insertOne(PaymentOrderDTO payOrderDTO) {
        return paymentOrderService.insertOne(ConvertBeanUtils.convert(payOrderDTO, PaymentOrderPO.class));
    }

    @Override
    public boolean updateOrderStatus(Long id, Integer status) {
        return paymentOrderService.updateOrderStatus(id, status);
    }

    @Override
    public boolean updateOrderStatus(String orderId, Integer status) {
        return paymentOrderService.updateOrderStatus(orderId, status);
    }

    @Override
    public boolean paymentCallback(PaymentOrderDTO payOrderDTO) {
        return paymentOrderService.paymentCallback(payOrderDTO);
    }
}
