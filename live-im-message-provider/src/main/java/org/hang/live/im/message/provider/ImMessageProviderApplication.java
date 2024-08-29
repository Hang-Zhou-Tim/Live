package org.hang.live.im.message.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author hang
 * @Date: Created in 17:21 2024/8/11
 * @Description
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableDubbo
public class ImMessageProviderApplication{
//    @Resource
//    ISmsService smsService;

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ImMessageProviderApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }

}