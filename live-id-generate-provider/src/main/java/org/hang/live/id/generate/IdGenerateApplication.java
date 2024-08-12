package org.hang.live.id.generate;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.hang.live.id.generate.service.IdGenerateService;
import org.hang.live.id.generate.service.impl.IdGenerateServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


/**
 * @Author hang
 * @Date: Created in 19:45 2023/5/25
 * @Description
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableDubbo
public class IdGenerateApplication /* implements CommandLineRunner*/ {

//    @Resource
//    private IdGenerateService idGenerateService;

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(IdGenerateApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }

//    @Override
//    public void run(String... args) throws Exception{
//        for (int i = 0; i < 1000; i++) {
//            Long id = idGenerateService.getSeqId(1);
//            System.out.println(id);
//        }
//    }
}
