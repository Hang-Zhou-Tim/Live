package org.hang.live.user.provider.service;

import org.hang.live.user.dto.UserDTO;

import java.util.List;
import java.util.Map;

/**
 * @Author idea
 * @Date: Created in 16:40 2023/5/12
 * @Description
 */
public interface IUserService {

    UserDTO getByUserId(Long userId);

    boolean updateUserInfo(UserDTO userDTO);

    boolean insertOne(UserDTO userDTO);

    Map<Long,UserDTO> batchQueryUserInfo(List<Long> userIdList);
}
