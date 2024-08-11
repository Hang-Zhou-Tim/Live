package org.hang.live.api.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.hang.live.user.dto.UserDTO;
import org.hang.live.user.interfaces.IUserRPC;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/live/api/user")
public class UserController {
    @DubboReference
    private IUserRPC userRPC;

    @GetMapping("/getUserInfo")
    public UserDTO getUserById(Long userId){
        return userRPC.getByUserId(userId);
    }
    @GetMapping("/insertUserInfo")
    public boolean insertUser(UserDTO user){
        return userRPC.insertOne(user);
    }
    @GetMapping("/updateUserInfo")
    public boolean updateUserInfo(UserDTO user){
        return userRPC.updateUserInfo(user);
    }
    @GetMapping("/batchQueryUserInfo")
    public Map<Long,UserDTO> batchQueryUserInfo(String userIdStr){
        return userRPC.batchQueryUserInfo(Arrays.asList(userIdStr.split(",")).stream().map(x -> Long.valueOf(x)).collect(Collectors.toList()));
    }
}
