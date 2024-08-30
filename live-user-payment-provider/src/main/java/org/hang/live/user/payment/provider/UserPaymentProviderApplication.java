package org.hang.live.user.payment.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
@SpringBootApplication
@EnableDubbo
public class UserPaymentProviderApplication  {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(UserPaymentProviderApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }
}
