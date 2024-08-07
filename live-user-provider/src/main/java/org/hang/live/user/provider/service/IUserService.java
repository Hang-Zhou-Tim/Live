package org.hang.live.user.provider.service;

import org.hang.user.dto.UserDTO;

public interface IUserService {
    public UserDTO getByUserId(Long userId);
}
