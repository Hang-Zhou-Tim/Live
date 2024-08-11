package org.hang.live.user.provider.service;

import org.hang.live.user.dto.UserLoginDTO;
import org.hang.live.user.dto.UserPhoneDTO;

import java.util.List;

/**
 * @Author hang
 * @Date: Created in 17:21 2024/8/11
 * @Description
 */
public interface IUserPhoneService {

    /**
     * User Login and register if there is no account
     *
     * @param phone
     * @return
     */
    UserLoginDTO login(String phone);

    /**
     * Check user info by phone
     *
     * @param phone
     * @return
     */
    UserPhoneDTO queryByPhone(String phone);

    /**
     * Check user phones info by user id
     *
     * @param userId
     * @return
     */
    List<UserPhoneDTO> queryByUserId(Long userId);
}
