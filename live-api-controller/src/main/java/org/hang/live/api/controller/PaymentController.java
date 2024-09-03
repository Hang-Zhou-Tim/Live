package org.hang.live.api.controller;

import jakarta.annotation.Resource;
import org.hang.live.api.service.IPaymentService;
import org.hang.live.api.vo.req.BuyCurrencyReqVO;
import org.hang.live.common.interfaces.vo.WebResponseVO;
import org.hang.live.common.web.configuration.error.BizBaseErrorEnum;
import org.hang.live.common.web.configuration.error.ErrorAssert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/25
 * @Description
 */
@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Resource
    private IPaymentService paymentService;

    @PostMapping("/getAllCurrencyAmounts")
    public WebResponseVO getAllCurrencyAmounts(Integer type) {
        ErrorAssert.isNotNull(type, BizBaseErrorEnum.PARAM_ERROR);
        return WebResponseVO.success(paymentService.getAllCurrencyAmounts(type));
    }

    @PostMapping("/buyCurrency")
    public WebResponseVO buyCurrency(BuyCurrencyReqVO buyCurrencyReqVO) {
        return WebResponseVO.success(paymentService.buyCurrency(buyCurrencyReqVO));
    }

}
