package org.hang.live.payment.callback.controller;

import jakarta.annotation.Resource;
import org.hang.live.payment.callback.service.IPayNotifyService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simulation of Callback for Third Party Payment Finished.
 * Since I cannot be verified as a organisation, I cannot get an thrid-party payment api.
 * So I can only simulate the payment process and assume the payment is always finished,
 * thus this callback will be triggered.
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
@RestController
@RequestMapping("/paymentNotify")
public class PayNotifyController {

    @Resource
    private IPayNotifyService payNotifyService;

    @PostMapping("/callback")
    public String paymentCallBack(@RequestParam("param") String param) {
        return payNotifyService.notifyHandler(param);
    }

}
