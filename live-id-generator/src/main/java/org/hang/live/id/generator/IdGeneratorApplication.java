package org.hang.live.id.generator;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.io.IOException;


/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/11
 * @Description
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableDubbo
public class IdGeneratorApplication /* implements CommandLineRunner*/ {

//    @Resource
//    private IdGenerateService idGenerateService;

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(IdGeneratorApplication.class);
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

//    @Override
//    public void run(String... args) throws Exception{
//        for (int i = 0; i < 1000; i++) {
//            Long id = idGenerateService.getSeqId(1);
//            System.out.println(id);
//        }
//    }
}
