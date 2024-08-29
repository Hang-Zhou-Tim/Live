package org.hang.live.stream.room.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author hang
 * @Date: Created in 21:22 2024/8/14
 * @Description
 */
@SpringBootApplication
@EnableDubbo
public class LiveStreamRoomProviderApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(LiveStreamRoomProviderApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }
}
