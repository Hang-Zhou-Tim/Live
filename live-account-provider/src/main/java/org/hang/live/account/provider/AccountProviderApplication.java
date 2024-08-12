package org.hang.live.account.provider;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.hang.live.account.provider.service.impl.AccountTokenServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.io.IOException;

/**
 * @Author hang
 * @Date: Created in 10:13 2024/8/11
 * @Description
 */
@SpringBootApplication
@EnableDubbo
@EnableDiscoveryClient
public class AccountProviderApplication /* implements CommandLineRunner */ {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(AccountProviderApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
        try{
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    @Override
//    public void run(String... args) throws Exception {
//        String token = accountTokenService.createAndSaveLoginToken(1000001L);
//        long userId = accountTokenService.getUserIdByToken(token);
//        System.out.println("Token"+ token + "userid" + userId);
//    }
}
