package org.hang.live.user.payment.provider.rpc;

import com.alibaba.fastjson2.JSON;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.hang.live.user.payment.dto.PaymentCallbackDTO;
import org.hang.live.user.payment.dto.PaymentOrderDTO;
import org.hang.live.user.payment.interfaces.IPaymentCallbackServiceRPC;
import org.hang.live.user.payment.interfaces.IPaymentOrderRPC;
import org.springframework.stereotype.Service;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
@DubboService(timeout = 3000)
public class PaymentCallbackServiceRPCImpl implements IPaymentCallbackServiceRPC {

    @DubboReference
    private IPaymentOrderRPC paymentOrderRpc;

    @Override
    public String paymentCallback(String paramJson) {
        PaymentCallbackDTO paymentCallbackVO = JSON.parseObject(paramJson, PaymentCallbackDTO.class);
        PaymentOrderDTO paymentOrderDTO = new PaymentOrderDTO();
        paymentOrderDTO.setUserId(paymentCallbackVO.getUserId());
        paymentOrderDTO.setBizCode(paymentCallbackVO.getBizCode());
        paymentOrderDTO.setOrderId(paymentCallbackVO.getOrderId());
        return paymentOrderRpc.paymentCallback(paymentOrderDTO) ? "success" : "fail";
    }
}
