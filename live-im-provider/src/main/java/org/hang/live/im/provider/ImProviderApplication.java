package org.hang.live.im.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author hang
 * @Date: Created in 21:02 2024/8/12
 * @Description
 */
@SpringBootApplication
@EnableDubbo
public class ImProviderApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ImProviderApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }
}
