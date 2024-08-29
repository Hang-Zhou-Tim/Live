package org.hang.live.im.core.server.service.impl;

import jakarta.annotation.Resource;
import org.hang.live.im.core.server.interfaces.constants.ImCoreServerConstants;
import org.hang.live.im.core.server.service.ImOnlineService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Author hang
 * @Date: Created in 09:30 2024/8/12
 * @Description
 */
@Service
public class ImOnlineServiceImpl implements ImOnlineService {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public boolean isOnline(long userId, int appId) {
        return redisTemplate.hasKey(ImCoreServerConstants.IM_BIND_IP_KEY + appId + ":" + userId);
    }

}
