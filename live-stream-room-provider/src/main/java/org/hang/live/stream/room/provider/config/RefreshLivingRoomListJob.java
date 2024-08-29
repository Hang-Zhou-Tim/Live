package org.hang.live.stream.room.provider.config;

import jakarta.annotation.Resource;
import org.hang.live.stream.room.provider.service.ILivingRoomService;
import org.hang.live.common.redis.configuration.key.StreamRoomProviderCacheKeyBuilder;
import org.hang.live.stream.room.interfaces.constants.LivingRoomTypeEnum;
import org.hang.live.stream.room.interfaces.dto.LivingRoomRespDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * When the service started, refresh live room user ids for every 1s
 *
 * @Author hang
 * @Date: Created in 18:56 2024/8/14
 * @Description
 */
@Configuration
public class RefreshLivingRoomListJob implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshLivingRoomListJob.class);

    @Resource
    private ILivingRoomService livingRoomService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private StreamRoomProviderCacheKeyBuilder cacheKeyBuilder;

    private ScheduledThreadPoolExecutor schedulePool = new ScheduledThreadPoolExecutor(1);

    @Override
    public void afterPropertiesSet() throws Exception {
        //Every one second refresh the room's user list.
        schedulePool.scheduleWithFixedDelay(new RefreshCacheListJob(), 3000, 1000, TimeUnit.MILLISECONDS);
    }

    class RefreshCacheListJob implements Runnable {

        @Override
        public void run() {
            String cacheKey = cacheKeyBuilder.buildRefreshLiveStreamRoomListLock();
            //To avoid in distributed environment different JVM process updates the same key and value in Redis, we lock it so that only one process can update them once.
            boolean lockStatus = redisTemplate.opsForValue().setIfAbsent(cacheKey, 1, 1, TimeUnit.SECONDS);
            if (lockStatus) {
                LOGGER.debug("[RefreshLivingRoomListJob] starting load db record of live stream rooms to redis");
                refreshDBToRedis(LivingRoomTypeEnum.DEFAULT_LIVING_ROOM.getCode());
                refreshDBToRedis(LivingRoomTypeEnum.PK_LIVING_ROOM.getCode());
                LOGGER.debug("[RefreshLivingRoomListJob] end load db record of live stream rooms to redis");
            }
        }
    }

    private void refreshDBToRedis(Integer type) {
        String cacheKey = cacheKeyBuilder.buildLiveStreamRoomList(type);
        //query all room every 1s
        List<LivingRoomRespDTO> resultList = livingRoomService.listAllLivingRoomFromDB(type);
        if(CollectionUtils.isEmpty(resultList)) {
            redisTemplate.delete(cacheKey);
            return;
        }
        //Notice that when we refresh, we do not clear previous redis list and then add each new keys to redis
        //we create a temp redis list, add new rooms, and then rename the key of that list.
        // By this way, we avoid huge amount of key expired at same time.
        String tempListName = cacheKey + "_temp";
        //Based on order of query result, add to Redis.
        for (LivingRoomRespDTO livingRoomRespDTO : resultList) {
            redisTemplate.opsForList().rightPush(tempListName, livingRoomRespDTO);
        }
        //rename the key
        redisTemplate.rename(tempListName, cacheKey);
        redisTemplate.delete(tempListName);
    }
}
