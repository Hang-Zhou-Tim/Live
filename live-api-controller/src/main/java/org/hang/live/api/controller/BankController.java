package org.hang.live.api.controller;

import jakarta.annotation.Resource;
import org.hang.live.api.service.IBankService;
import org.hang.live.api.vo.req.PayProductReqVO;
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
@RequestMapping("/bank")
public class BankController {

    @Resource
    private IBankService bankService;

    @PostMapping("/products")
    public WebResponseVO products(Integer type) {
        ErrorAssert.isNotNull(type, BizBaseErrorEnum.PARAM_ERROR);
        return WebResponseVO.success(bankService.products(type));
    }

    @PostMapping("/payProduct")
    public WebResponseVO payProduct(PayProductReqVO payProductReqVO) {
        return WebResponseVO.success(bankService.payProduct(payProductReqVO));
    }

}
