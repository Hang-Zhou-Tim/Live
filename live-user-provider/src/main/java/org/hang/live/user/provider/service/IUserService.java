package org.hang.live.user.provider.service;

import org.hang.live.user.dto.UserDTO;

import java.util.List;
import java.util.Map;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/21
 * @Description
 */
public interface IUserService {

    UserDTO getByUserId(Long userId);

    boolean updateUserInfo(UserDTO userDTO);

    boolean insertOne(UserDTO userDTO);

    Map<Long,UserDTO> batchQueryUserInfo(List<Long> userIdList);
}
