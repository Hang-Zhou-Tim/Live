package org.hang.live.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/24
 * @Description
 */
@SpringBootApplication  //Auto Load Packages and Context
@EnableDiscoveryClient  //Enable Service Discovery
public class ApiControllerApplication /*implements CommandLineRunner*/ {
    public static void main(String[] args) {
        //Initialise spring application with this class
        SpringApplication springApplication = new SpringApplication(ApiControllerApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.SERVLET); //None means it will not work as servlet-based web service, and not initialise Tomcat;
        springApplication.run(args);
    }

//    @Override
//    public void run(String... args) throws Exception {
//        System.out.println(RequestContext.get(RequestConstants.HANG_USER_ID));
//    }
}
