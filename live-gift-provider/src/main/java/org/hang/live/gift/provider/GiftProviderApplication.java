package org.hang.live.gift.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
@SpringBootApplication
@EnableDubbo
@EnableDiscoveryClient
public class GiftProviderApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(GiftProviderApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }

}
