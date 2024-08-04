package org.hang.live.api.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.hang.user.interfaces.IUserRPC;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @DubboReference
    private IUserRPC userRPC;

    @GetMapping("/dubbo")
    public String dubbo(){
        userRPC.test();
        return "success";
    }
}
