package org.hang.live.payment.callback.service.impl;

import com.alibaba.fastjson2.JSON;
import org.apache.dubbo.config.annotation.DubboReference;
import org.hang.live.payment.callback.service.IPaymentCallbackService;
import org.hang.live.payment.callback.vo.PaymentCallbackVO;
import org.hang.live.user.payment.dto.PaymentOrderDTO;
import org.hang.live.user.payment.interfaces.IPaymentOrderRPC;
import org.springframework.stereotype.Service;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
@Service
public class PaymentCallbackServiceImpl implements IPaymentCallbackService {

    @DubboReference
    private IPaymentOrderRPC payOrderRpc;

    @Override
    public String paymentCallback(String paramJson) {
        PaymentCallbackVO wxPayNotifyVO = JSON.parseObject(paramJson, PaymentCallbackVO.class);
        PaymentOrderDTO paymentOrderDTO = new PaymentOrderDTO();
        paymentOrderDTO.setUserId(wxPayNotifyVO.getUserId());
        paymentOrderDTO.setBizCode(wxPayNotifyVO.getBizCode());
        paymentOrderDTO.setOrderId(wxPayNotifyVO.getOrderId());
        return payOrderRpc.paymentCallback(paymentOrderDTO) ? "success" : "fail";
    }
}
