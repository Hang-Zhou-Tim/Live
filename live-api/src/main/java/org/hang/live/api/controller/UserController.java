package org.hang.live.api.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.hang.user.dto.UserDTO;
import org.hang.user.interfaces.IUserRPC;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @DubboReference
    private IUserRPC userRPC;

    @GetMapping("/getUserInfo")
    public UserDTO getUserById(Long userId){
        UserDTO check = userRPC.getByUserId(userId);
        System.out.println(check);
        return check;
    }
}
