package org.hang.live.msg.provider;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.graalvm.nativeimage.c.struct.RawField;
import org.hang.live.msg.enums.MsgSendResultEnum;
import org.hang.live.msg.provider.service.ISmsService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author idea
 * @Date: Created in 17:21 2023/6/11
 * @Description
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableDubbo
public class MsgProviderApplication /* implements CommandLineRunner */{
//    @Resource
//    ISmsService smsService;

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(MsgProviderApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }


//    @Override
//    public void run(String... args) throws Exception {
//        MsgSendResultEnum resultEnum = smsService.sendLoginCode("13141150122");
//
//    }
}