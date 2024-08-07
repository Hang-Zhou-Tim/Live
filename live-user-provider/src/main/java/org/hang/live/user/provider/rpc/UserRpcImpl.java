package org.hang.live.user.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.hang.live.user.provider.service.IUserService;
import org.hang.user.dto.UserDTO;
import org.hang.user.interfaces.IUserRPC;

@DubboService
public class UserRpcImpl implements IUserRPC {
    @Resource
    private IUserService userService;
    @Override
    public String test(){
        System.out.println("this is Dubbo test");
        return "success";
    }

    @Override
    public UserDTO getByUserId(Long userId) {
        return userService.getByUserId(userId);
    }

}
