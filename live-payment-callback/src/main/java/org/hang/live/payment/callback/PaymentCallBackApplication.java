package org.hang.live.payment.callback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
@SpringBootApplication
public class PaymentCallBackApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(PaymentCallBackApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.SERVLET);
        springApplication.run(args);
    }
}
