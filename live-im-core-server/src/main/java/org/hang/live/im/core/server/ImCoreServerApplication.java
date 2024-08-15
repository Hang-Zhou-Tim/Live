package org.hang.live.im.core.server;


import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The starter of netty server that initialises IM core modules
 *
 * @Author hang
 * @Date: Created in 22:02 2024/8/11
 * @Description
 */
@SpringBootApplication
@EnableDubbo
public class ImCoreServerApplication {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication springApplication = new SpringApplication(ImCoreServerApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }
}
