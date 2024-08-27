package org.hang.live.im.provider.service.impl;

import jakarta.annotation.Resource;
import org.hang.live.framework.redis.starter.key.ImProviderCacheKeyBuilder;
import org.hang.live.im.provider.service.ImTokenService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author hang
 * @Date: Created in 09:30 2024/8/12
 * @Description
 */
@Service
public class ImTokenServiceImpl implements ImTokenService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private ImProviderCacheKeyBuilder cacheKeyBuilder;

    @Override
    public String createImLoginToken(long userId, int appId) {
        String token = UUID.randomUUID() + "%" + appId;
        redisTemplate.opsForValue().set(cacheKeyBuilder.buildImLoginTokenKey(token), userId, 5, TimeUnit.MINUTES);
        return token;
    }

    @Override
    public Long getUserIdByToken(String token) {
        Object userId = redisTemplate.opsForValue().get(cacheKeyBuilder.buildImLoginTokenKey(token));
        return userId == null ? null : Long.valueOf((Integer) userId);
    }

}