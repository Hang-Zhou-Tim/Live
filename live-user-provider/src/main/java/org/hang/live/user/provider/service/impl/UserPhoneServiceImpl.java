package org.hang.live.user.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.hang.live.common.redis.configuration.key.UserProviderCacheKeyBuilder;
import org.hang.live.common.interfaces.utils.ConvertBeanUtils;
import org.hang.live.common.interfaces.utils.DESUtils;
import org.hang.live.id.generator.interfaces.IdGenerateRPC;
import org.hang.live.id.generator.interfaces.enums.IdTypeEnum;
import org.hang.live.user.dto.UserDTO;
import org.hang.live.user.dto.UserLoginDTO;
import org.hang.live.common.interfaces.enums.CommonStatusEnum;
import org.hang.live.user.dto.UserPhoneDTO;
import org.hang.live.user.provider.dao.mapper.IUserPhoneMapper;
import org.hang.live.user.provider.dao.po.UserPhonePO;
import org.hang.live.user.provider.service.IUserPhoneService;
import org.hang.live.user.provider.service.IUserService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author hang
 * @Date: Created in 17:21 2024/8/21
 * @Description
 */
@Service
public class UserPhoneServiceImpl implements IUserPhoneService {

    @Resource
    private IUserPhoneMapper userPhoneMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private UserProviderCacheKeyBuilder cacheKeyBuilder;
    @Resource
    private IUserService userService;
    @DubboReference
    private IdGenerateRPC idGenerateRpc;

    @Override
    public UserLoginDTO login(String phone) {
        //phone cannot be null
        if (StringUtils.isEmpty(phone)) {
            return null;
        }
        //Check if the phone registered
        UserPhoneDTO userPhoneDTO = this.queryByPhone(phone);
        if (userPhoneDTO != null) {
            return UserLoginDTO.loginSuccess(userPhoneDTO.getUserId());
        }
        //If not registered before, register now.
        return registerAndLogin(phone);
    }

    /**
     * Register the user id and phone
     *
     * @param phone
     */
    private UserLoginDTO registerAndLogin(String phone) {
        Long userId = idGenerateRpc.getUnSeqId(IdTypeEnum.USER_ID.getCode());
        UserDTO userDTO = new UserDTO();
        userDTO.setNickName("HL-User-" + userId);
        userDTO.setUserId(userId);
        userDTO.setAvatar("../svga/img/angel.png");
        userService.insertOne(userDTO);
        UserPhonePO userPhonePO = new UserPhonePO();
        userPhonePO.setUserId(userId);
        //Encrypt to protect user information.
        userPhonePO.setPhone(DESUtils.encrypt(phone));
        userPhonePO.setStatus(CommonStatusEnum.VALID_STATUS.getCode());
        userPhoneMapper.insert(userPhonePO);
        redisTemplate.delete(cacheKeyBuilder.buildUserPhoneObjKey(phone));
        return UserLoginDTO.loginSuccess(userId);
    }
    //Query user by phoneId
    @Override
    public UserPhoneDTO queryByPhone(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return null;
        }
        String redisKey = cacheKeyBuilder.buildUserPhoneObjKey(phone);
        //Check if the user has phone recorded in redis
        UserPhoneDTO userPhoneDTO = (UserPhoneDTO) redisTemplate.opsForValue().get(redisKey);
        if (userPhoneDTO != null) {
            //If it is empty object, return null
            if (userPhoneDTO.getUserId() == null) {
                return null;
            }
            return userPhoneDTO;
        }
        //If there is no record in redis, search in MySQL. Notice that when we insert phone number, we actually encrypt it.
        userPhoneDTO = this.queryByPhoneFromDB(phone);
        if (userPhoneDTO != null) {
            userPhoneDTO.setPhone(DESUtils.decrypt(userPhoneDTO.getPhone()));
            redisTemplate.opsForValue().set(redisKey, userPhoneDTO, 30, TimeUnit.MINUTES);
            return userPhoneDTO;
        }
        //Avoid Caching Attack by recording empty object for the id.
        userPhoneDTO = new UserPhoneDTO();
        redisTemplate.opsForValue().set(redisKey, userPhoneDTO, 5, TimeUnit.MINUTES);
        return null;
    }


    @Override
    public List<UserPhoneDTO> queryByUserId(Long userId) {
        //Similar Logic, just notice that one userid can relate to multiple phones
        if (userId == null || userId < 10000) {
            return Collections.emptyList();
        }
        String redisKey = cacheKeyBuilder.buildUserPhoneListKey(userId);
        List<Object> userPhoneList = redisTemplate.opsForList().range(redisKey, 0, -1);
        if (!CollectionUtils.isEmpty(userPhoneList)) {
            if (((UserPhoneDTO) userPhoneList.get(0)).getUserId() == null) {
                return Collections.emptyList();
            }
            return userPhoneList.stream().map(x -> (UserPhoneDTO) x).collect(Collectors.toList());
        }
        List<UserPhoneDTO> userPhoneDTOS = this.queryByUserIdFromDB(userId);
        if (!CollectionUtils.isEmpty(userPhoneDTOS)) {
            userPhoneDTOS.stream().forEach(x -> x.setPhone(DESUtils.decrypt(x.getPhone())));
            redisTemplate.opsForList().leftPushAll(redisKey, userPhoneDTOS.toArray());
            redisTemplate.expire(redisKey, 30, TimeUnit.MINUTES);
            return userPhoneDTOS;
        }
        //Recording empty object for invalid query.
        redisTemplate.opsForList().leftPush(redisKey, new UserPhoneDTO());
        redisTemplate.expire(redisKey, 5, TimeUnit.MINUTES);
        return Collections.emptyList();
    }


    private List<UserPhoneDTO> queryByUserIdFromDB(Long userId) {
        LambdaQueryWrapper<UserPhonePO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserPhonePO::getUserId, userId);
        queryWrapper.eq(UserPhonePO::getStatus, CommonStatusEnum.VALID_STATUS.getCode());
        queryWrapper.last("limit 1");
        return ConvertBeanUtils.convertList(userPhoneMapper.selectList(queryWrapper), UserPhoneDTO.class);
    }

    private UserPhoneDTO queryByPhoneFromDB(String phone) {
        LambdaQueryWrapper<UserPhonePO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserPhonePO::getPhone, DESUtils.encrypt(phone));
        queryWrapper.eq(UserPhonePO::getStatus, CommonStatusEnum.VALID_STATUS.getCode());
        queryWrapper.last("limit 1");
        return ConvertBeanUtils.convert(userPhoneMapper.selectOne(queryWrapper), UserPhoneDTO.class);
    }
}
