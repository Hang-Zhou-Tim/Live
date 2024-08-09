package org.hang.live.user.provider.service.impl;

import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.producer.MQProducer;
import org.hang.live.common.interfaces.topic.UserProviderTopicNames;
import org.apache.rocketmq.common.message.Message;
import org.hang.live.framework.redis.starter.key.UserProviderCacheKeyBuilder;
import org.hang.live.common.interfaces.utils.ConvertBeanUtils;
import org.hang.live.user.constants.CacheAsyncDeleteCode;
import org.hang.live.user.constants.UserTagFieldNameConstants;
import org.hang.live.user.constants.UserTagsEnum;
import org.hang.live.user.dto.UserCacheAsyncDeleteDTO;
import org.hang.live.user.dto.UserTagDTO;
import org.hang.live.user.provider.dao.mapper.IUserTagMapper;
import org.hang.live.user.provider.dao.po.UserTagPO;
import org.hang.live.user.provider.service.IUserTagService;
import org.hang.live.user.utils.TagInfoUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author hang
 * @Date: Created in 17:13 2023/5/27
 * @Description
 */
@Service
public class UserTagServiceImpl implements IUserTagService {

    @Resource
    private IUserTagMapper userTagMapper;
    @Resource
    private RedisTemplate<String, UserTagDTO> redisTemplate;
    @Resource
    private UserProviderCacheKeyBuilder cacheKeyBuilder;
    @Resource
    private MQProducer mqProducer;

    @Override
    public boolean setTag(Long userId, UserTagsEnum userTagsEnum) {
        //Update the tag if it exists
        boolean updateStatus = userTagMapper.setTag(userId, userTagsEnum.getFieldName(), userTagsEnum.getTag()) > 0;
        if (updateStatus) {
            deleteUserTagDTOFromRedis(userId);
            return true;
        }
        //If the key existed, but it is not being updated, return false.
        UserTagPO userTagPO = userTagMapper.selectById(userId);
        if (userTagPO != null) {
            return false;
        }
        //If the key is not existed, before inserting new rows, cache this operation to avoid duplicate insertion of another threads.
        String setNxKey = cacheKeyBuilder.buildTagLockKey(userId);
        String setNxResult = redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer keySerializer = redisTemplate.getKeySerializer();
                RedisSerializer valueSerializer = redisTemplate.getValueSerializer();
                return (String) connection.execute("set", keySerializer.serialize(setNxKey),
                        valueSerializer.serialize("-1"),
                        "NX".getBytes(StandardCharsets.UTF_8),
                        "EX".getBytes(StandardCharsets.UTF_8),
                        "3".getBytes(StandardCharsets.UTF_8));
            }
        });
        //If the key is already cached for inserting the user tag, avoid duplicate inserting.
        if (!"OK".equals(setNxResult)) {
            return false;
        }
        userTagPO = new UserTagPO();
        userTagPO.setUserId(userId);
        userTagMapper.insert(userTagPO);
        updateStatus = userTagMapper.setTag(userId, userTagsEnum.getFieldName(), userTagsEnum.getTag()) > 0;
        redisTemplate.delete(setNxKey);
        return updateStatus;
    }
    //First delete tags in Mysql, then double deletes them in Redis.
    @Override
    public boolean cancelTag(Long userId, UserTagsEnum userTagsEnum) {
        boolean cancelStatus = userTagMapper.cancelTag(userId, userTagsEnum.getFieldName(), userTagsEnum.getTag()) > 0;
        if (!cancelStatus) {
            return false;
        }
        deleteUserTagDTOFromRedis(userId);
        return true;
    }

    //Check if the user has the tag.
    @Override
    public boolean containTag(Long userId, UserTagsEnum userTagsEnum) {
        UserTagDTO userTagDTO = this.queryByUserIdFromRedis(userId);
        if (userTagDTO == null) {
            return false;
        }
        String queryFieldName = userTagsEnum.getFieldName();
        // Find the expected binary fields, check if it is contained by that user.
        if (UserTagFieldNameConstants.TAG_INFO_01.equals(queryFieldName)) {
            return TagInfoUtils.isContain(userTagDTO.getTagInfo01(), userTagsEnum.getTag());
        } else if (UserTagFieldNameConstants.TAG_INFO_02.equals(queryFieldName)) {
            return TagInfoUtils.isContain(userTagDTO.getTagInfo02(), userTagsEnum.getTag());
        } else if (UserTagFieldNameConstants.TAG_INFO_03.equals(queryFieldName)) {
            return TagInfoUtils.isContain(userTagDTO.getTagInfo03(), userTagsEnum.getTag());
        }
        return false;
    }

    /**
     * Delete user tags from redis. This implements delayed double deletion.
     *
     * @param userId
     */
    private void deleteUserTagDTOFromRedis(Long userId) {
        String redisKey = cacheKeyBuilder.buildTagKey(userId);
        redisTemplate.delete(redisKey);

        //Double delayed deletion in RocketMQ.
        UserCacheAsyncDeleteDTO userCacheAsyncDeleteDTO = new UserCacheAsyncDeleteDTO();
        userCacheAsyncDeleteDTO.setCode(CacheAsyncDeleteCode.USER_TAG_DELETE.getCode());
        Map<String,Object> jsonParam = new HashMap<>();
        jsonParam.put("userId",userId);
        userCacheAsyncDeleteDTO.setJson(JSON.toJSONString(jsonParam));

        Message message = new Message();
        message.setTopic(UserProviderTopicNames.CACHE_ASYNC_DELETE_TOPIC);
        message.setBody(JSON.toJSONString(userCacheAsyncDeleteDTO).getBytes());
        //Delayed deletion after 1 second.
        message.setDelayTimeLevel(1);
        try {
            mqProducer.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Query User Tag by User ID in Redis. If it is not existed, then query Mysql and put results into Redis.
     *
     * @param userId
     * @return
     */
    private UserTagDTO queryByUserIdFromRedis(Long userId) {
        String redisKey = cacheKeyBuilder.buildTagKey(userId);
        UserTagDTO userTagDTO = redisTemplate.opsForValue().get(redisKey);
        if (userTagDTO != null) {
            return userTagDTO;
        }
        UserTagPO userTagPO = userTagMapper.selectById(userId);
        if (userTagPO == null) {
            return null;
        }
        userTagDTO = ConvertBeanUtils.convert(userTagPO, UserTagDTO.class);
        redisTemplate.opsForValue().set(redisKey, userTagDTO);
        redisTemplate.expire(redisKey,30, TimeUnit.MINUTES);
        return userTagDTO;
    }
}
