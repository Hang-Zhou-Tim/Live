package org.hang.live.user.interfaces;

import org.hang.live.user.dto.UserLoginDTO;
import org.hang.live.user.dto.UserPhoneDTO;

import java.util.List;

/**
 * User phone related RPC
 *
 * @Author hang
 * @Date: Created in 17:17 2024/8/11
 * @Description
 */
public interface IUserPhoneRPC {

    /**
     * User Login/Register
     * @param phone
     * @return
     */
    UserLoginDTO login(String phone);

    /**
     * Query User Info by User Phone
     *
     * @param phone
     * @return
     */
    UserPhoneDTO queryByPhone(String phone);

    /**
     * Query User Info by User ID
     *
     * @param userId
     * @return
     */
    List<UserPhoneDTO> queryByUserId(Long userId);
}
