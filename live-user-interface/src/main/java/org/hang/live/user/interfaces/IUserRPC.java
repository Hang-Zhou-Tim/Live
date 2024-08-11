package org.hang.live.user.interfaces;

import org.hang.live.user.dto.UserDTO;

import java.util.List;
import java.util.Map;

/**
 * @Author hang
 * @Description
 */
public interface IUserRPC {

    UserDTO getByUserId(Long userId);

    boolean updateUserInfo(UserDTO userDTO);

    boolean insertOne(UserDTO userDTO);

    Map<Long,UserDTO> batchQueryUserInfo(List<Long> userIdList);
}
