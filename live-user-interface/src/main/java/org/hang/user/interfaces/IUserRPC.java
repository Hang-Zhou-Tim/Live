package org.hang.user.interfaces;

import org.hang.user.dto.UserDTO;

public interface IUserRPC {
    String test();
    public UserDTO getByUserId(Long userId);

}
