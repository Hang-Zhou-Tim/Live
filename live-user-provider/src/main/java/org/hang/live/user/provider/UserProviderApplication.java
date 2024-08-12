package org.hang.live.user.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.hang.live.user.constants.UserTagsEnum;
import org.hang.live.user.dto.UserDTO;
import org.hang.live.user.provider.service.IUserService;
import org.hang.live.user.provider.service.IUserTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication  //Auto Load Packages and Context
@EnableDubbo            //Decode Configuration, Scan and Initialise Provider Services(with Proxy).
@EnableDiscoveryClient  //Enable Service Discovery
public class UserProviderApplication /* implements CommandLineRunner*/ {
//    @Autowired
//    IUserService userService;
//
//    @Autowired
//    IUserTagService userTagService;

//    @Override
//    public void run(String... args) throws Exception {
//        Long userId = 1004L;
//        UserDTO userDTO = userService.getByUserId(userId);
        //userDTO.setNickName("abc");

//        System.out.println(userTagService.containTag(userId, UserTagsEnum.IS_OLD_USER));
//        System.out.println(userTagService.setTag(userId, UserTagsEnum.IS_OLD_USER));
//        System.out.println(userTagService.containTag(userId, UserTagsEnum.IS_OLD_USER));
//        System.out.println(userTagService.cancelTag(userId, UserTagsEnum.IS_OLD_USER));
//        System.out.println(userTagService.containTag(userId, UserTagsEnum.IS_OLD_USER));
//        for(int i=0;i<100;++i){
//            new Thread(()->{
//                userTagService.setTag(userId, UserTagsEnum.IS_OLD_USER);
//            }).start();
//        }



    public static void main(String[] args) {
        //Initialise spring application with this class
        SpringApplication springApplication = new SpringApplication(UserProviderApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE); //None means it will not work as servlet-based web service, and not initialise Tomcat;
        springApplication.run(args);
    }



}
