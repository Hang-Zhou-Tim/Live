package org.hang.live.payment.callback.service.impl;

import com.alibaba.fastjson2.JSON;
import org.apache.dubbo.config.annotation.DubboReference;
import org.hang.live.payment.callback.service.IPayNotifyService;
import org.hang.live.payment.callback.vo.PaymentCallbackVO;
import org.hang.live.user.payment.dto.PayOrderDTO;
import org.hang.live.user.payment.interfaces.IPayOrderRpc;
import org.springframework.stereotype.Service;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
@Service
public class PayNotifyServiceImpl implements IPayNotifyService {

    @DubboReference
    private IPayOrderRpc payOrderRpc;

    @Override
    public String notifyHandler(String paramJson) {
        PaymentCallbackVO wxPayNotifyVO = JSON.parseObject(paramJson, PaymentCallbackVO.class);
        PayOrderDTO payOrderDTO = new PayOrderDTO();
        payOrderDTO.setUserId(wxPayNotifyVO.getUserId());
        payOrderDTO.setBizCode(wxPayNotifyVO.getBizCode());
        payOrderDTO.setOrderId(wxPayNotifyVO.getOrderId());
        return payOrderRpc.payNotify(payOrderDTO) ? "success" : "fail";
    }
}
