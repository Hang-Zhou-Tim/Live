package org.hang.live.user.provider.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.common.message.Message;
import org.hang.live.common.interfaces.topic.UserProviderTopicNames;
import org.hang.live.common.interfaces.utils.ConvertBeanUtils;
import org.hang.live.common.redis.configuration.key.UserProviderCacheKeyBuilder;
import org.hang.live.user.constants.CacheAsyncDeleteCode;
import org.hang.live.user.dto.UserCacheAsyncDeleteDTO;
import org.hang.live.user.provider.dao.mapper.IUserMapper;
import org.hang.live.user.provider.dao.po.UserPO;
import org.hang.live.user.provider.service.IUserService;
import org.hang.live.user.dto.UserDTO;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/11
 * @Description
 */
@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private IUserMapper userMapper;
    @Resource
    private MQProducer mqProducer;

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
            redisTemplate.opsForValue().set(key,userDTO,30, TimeUnit.MINUTES);
        }
        return userDTO;
    }

    @Override
    public boolean updateUserInfo(UserDTO userDTO) {
        if(userDTO == null || userDTO.getUserId() == null){
            return false;
        }
        int updateStatus = userMapper.updateById(ConvertBeanUtils.convert(userDTO, UserPO.class));
        if (updateStatus > -1) {
            String key = userProviderCacheKeyBuilder.buildUserInfoKey(userDTO.getUserId());
            redisTemplate.delete(key);
            UserCacheAsyncDeleteDTO userCacheAsyncDeleteDTO = new UserCacheAsyncDeleteDTO();
            userCacheAsyncDeleteDTO.setCode(CacheAsyncDeleteCode.USER_INFO_DELETE.getCode());
            Map<String,Object> jsonParam = new HashMap<>();
            jsonParam.put("userId",userDTO.getUserId());
            userCacheAsyncDeleteDTO.setJson(JSON.toJSONString(jsonParam));
            Message message = new Message();
            message.setTopic(UserProviderTopicNames.CACHE_ASYNC_DELETE_TOPIC);
            message.setBody(JSON.toJSONString(userCacheAsyncDeleteDTO).getBytes());
            //Level 1: 1 second delay deletion
            message.setDelayTimeLevel(1);
            try {
                mqProducer.send(message);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }

    @Override
    public boolean insertOne(UserDTO userDTO) {
        if (userDTO == null || userDTO.getUserId() == null) {
            return false;
        }
        userMapper.insert(ConvertBeanUtils.convert(userDTO, UserPO.class));
        return true;
    }

    @Override
    public Map<Long, UserDTO> batchQueryUserInfo(List<Long> userIdList) {
        //1. Check if the userIdList is empty;
        if (CollectionUtils.isEmpty(userIdList)) {
            return Maps.newHashMap();
        }
        //1.1 Get the user id that is bigger than 10000 (Optional)
//        userIdList = userIdList.stream().filter(id -> id > 10000).collect(Collectors.toList());
//        if (CollectionUtils.isEmpty(userIdList)) {
//            return Maps.newHashMap();
//        }

        //2. Search all the batched key that is in redis
        List<String> keyList = new ArrayList<>();
        //2.1 serialise the key to the expected format
        userIdList.forEach(userId -> {
            keyList.add(userProviderCacheKeyBuilder.buildUserInfoKey(userId));
        });
        //2.1 find all values corresponding to the key, exclude those who are not in redis.
        List<UserDTO> userDTOList = redisTemplate.opsForValue().multiGet(keyList).stream().filter(x -> x != null).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(userDTOList) && userDTOList.size() == userIdList.size()) {
            return userDTOList.stream().collect(Collectors.toMap(UserDTO::getUserId, x -> x));
        }
        //2.2 Find the keys thar are not in the redis;
        Set<Long> userIdInCacheList = userDTOList.stream().map(UserDTO::getUserId).collect(Collectors.toSet());
        List<Long> userIdNotInCacheList = userIdList.stream().filter(x -> !userIdInCacheList.contains(x)).collect(Collectors.toList());


        //3. find all unmatched keys in mysql with multi-thread application.
        //3.1 Group keys id based on the sharding algorithm to get real table id to search
        Map<Long, List<Long>> userIdMap = userIdNotInCacheList.stream().collect(Collectors.groupingBy(userId -> userId % 100));
        List<UserDTO> dbQueryResult = new CopyOnWriteArrayList<>();
        //3.2 Use multiple threads to send request parallelly, increase the performance of MYSQL query under JDBC sharding algorithm(which works serially);
        userIdMap.values().parallelStream().forEach(queryUserIdList -> {
            dbQueryResult.addAll(ConvertBeanUtils.convertList(userMapper.selectBatchIds(queryUserIdList), UserDTO.class));
        });

        //4. Cache all batched keys that are not in Redis and searched by Mysql;
        if (!CollectionUtils.isEmpty(dbQueryResult)) {
            // 4.1 Save all key-and-value entries to be put in Redis;
            Map<String, UserDTO> saveCacheMap = dbQueryResult.stream().collect(Collectors.toMap(userDto -> userProviderCacheKeyBuilder.buildUserInfoKey(userDto.getUserId()), x -> x));
            // 4.2 Send those entries;
            redisTemplate.opsForValue().multiSet(saveCacheMap);
            // 4.3 Use pipline to add random seconds to avoid data expired at the same time.
            redisTemplate.executePipelined(new SessionCallback<Object>() {
                @Override
                public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                    for (String redisKey : saveCacheMap.keySet()) {
                        operations.expire((K) redisKey, createRandomTime(), TimeUnit.SECONDS);
                    }
                    return null;
                }
            });

            // 4.3 Append those db results into lists of DTO results;
            userDTOList.addAll(dbQueryResult);
        }
        //5. Return a map of keys and values based on the batched query.
        return userDTOList.stream().collect(Collectors.toMap(UserDTO::getUserId, x -> x));

    }

    private int createRandomTime() {
        int randomNumSecond = ThreadLocalRandom.current().nextInt(10000);
        return randomNumSecond + 30 * 60;
    }


}
