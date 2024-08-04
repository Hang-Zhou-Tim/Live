package org.hang.live.user.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication  //Auto Load Packages and Context
@EnableDubbo            //Decode Configuration, Scan and Initialise Provider Services(with Proxy).
@EnableDiscoveryClient  //Enable Service Discovery
public class UserProviderApplication {
    public static void main(String[] args) {
        //Initialise spring application with this class
        SpringApplication springApplication = new SpringApplication(UserProviderApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE); //None means it will not work as servlet-based web service, and not initialise Tomcat;
        springApplication.run(args);
    }

}
