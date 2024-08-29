package org.hang.live.im.server.router.provider;


import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

/**
 * @Author hang
 * @Date: Created in 16:19 2024/8/13
 * @Description
 */
@SpringBootApplication
@EnableDubbo
public class ImServerRouterProviderApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ImServerRouterProviderApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);

        try{
            //Using unlimited loops to prevent shutdown of main thread.
            while (true) {
                //prevent the main thread to die.
                Thread.sleep(Long.MAX_VALUE);
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
