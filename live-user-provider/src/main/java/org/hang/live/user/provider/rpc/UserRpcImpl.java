package org.hang.live.user.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.hang.live.user.dto.UserDTO;
import org.hang.live.user.interfaces.IUserRPC;
import org.hang.live.user.provider.service.IUserService;

import java.util.List;
import java.util.Map;

/**
 * @Author hang
 * @Date: Created in 15:42 2023/4/16
 * @Description
 */
@DubboService
public class UserRpcImpl implements IUserRPC {

    @Resource
    private IUserService userService;

    @Override
    public UserDTO getByUserId(Long userId) {
        return userService.getByUserId(userId);
    }

    @Override
    public boolean updateUserInfo(UserDTO userDTO) {
        return userService.updateUserInfo(userDTO);
    }

    @Override
    public boolean insertOne(UserDTO userDTO) {
        return userService.insertOne(userDTO);
    }

    @Override
    public Map<Long, UserDTO> batchQueryUserInfo(List<Long> userIdList) {
        return userService.batchQueryUserInfo(userIdList);
    }
}
