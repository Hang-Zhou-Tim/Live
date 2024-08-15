package org.hang.live.im.router.provider;


import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author hang
 * @Date: Created in 16:19 2024/8/13
 * @Description
 */
@SpringBootApplication
@EnableDubbo
public class ImRouterProviderApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ImRouterProviderApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }

}
