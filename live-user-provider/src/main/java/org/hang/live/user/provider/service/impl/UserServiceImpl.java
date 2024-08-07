package org.hang.live.user.provider.service.impl;

import jakarta.annotation.Resource;
import org.apache.catalina.User;
import org.hang.live.common.interfaces.utils.ConvertBeanUtils;
import org.hang.live.framework.redis.starter.key.UserProviderCacheKeyBuilder;
import org.hang.live.user.provider.dao.mapper.IUserMapper;
import org.hang.live.user.provider.service.IUserService;
import org.hang.user.dto.UserDTO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private IUserMapper userMapper;

    @Resource
    private RedisTemplate<String,UserDTO> redisTemplate;

    @Resource
    private UserProviderCacheKeyBuilder userProviderCacheKeyBuilder;
    @Override
    public UserDTO getByUserId(Long userId){
        if(userId == null){
            return null;
        }
        String key = userProviderCacheKeyBuilder.buildUserInfoKey(userId);
        UserDTO userDTO = redisTemplate.opsForValue().get(key);
        if(userDTO != null){
            return userDTO;
        }
        userDTO = ConvertBeanUtils.convert(userMapper.selectById(userId),UserDTO.class);
        if(userDTO!=null){
            redisTemplate.opsForValue().set(key,userDTO);
        }
        return userDTO;
    }
}
