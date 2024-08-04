package org.hang.live.api;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication  //Auto Load Packages and Context
@EnableDiscoveryClient  //Enable Service Discovery
public class ApiWebApplication {
    public static void main(String[] args) {
        //Initialise spring application with this class
        SpringApplication springApplication = new SpringApplication(ApiWebApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.SERVLET); //None means it will not work as servlet-based web service, and not initialise Tomcat;
        springApplication.run(args);
    }

}
