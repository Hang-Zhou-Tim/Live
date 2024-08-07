package org.hang.live.user.provider.service.impl;

import jakarta.annotation.Resource;
import org.hang.live.common.interfaces.utils.ConvertBeanUtils;
import org.hang.live.user.provider.dao.mapper.IUserMapper;
import org.hang.live.user.provider.service.IUserService;
import org.hang.user.dto.UserDTO;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private IUserMapper userMapper;

    @Override
    public UserDTO getByUserId(Long userId){
        if(userId == null){
            return null;
        }
        return ConvertBeanUtils.convert(userMapper.selectById(userId),UserDTO.class);
    }
}
