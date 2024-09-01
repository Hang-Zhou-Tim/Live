package org.hang.live.gift.provider.config;

import jakarta.annotation.Resource;
import org.hang.live.common.redis.configuration.key.GiftProviderCacheKeyBuilder;
import org.hang.live.gift.interfaces.ISkuStockInfoRPC;
import org.hang.live.gift.provider.service.IAnchorShopInfoService;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/30
 * @Description
 */
@Configuration
@EnableScheduling
public class RefreshStockNumConfig {

    @Resource
    private ISkuStockInfoRPC skuStockInfoRPC;
    @Resource
    private IAnchorShopInfoService anchorShopInfoService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private GiftProviderCacheKeyBuilder cacheKeyBuilder;

    //Every 15 second, update all sku stocks that anchors are currently selling to redis.
    // (This might create inconsistency in database because stock number keeps changing)
    @Scheduled(cron = "*/15 * * * * ? ")
    public void refreshStockNum() {
        String lockKey = cacheKeyBuilder.buildStockSyncLock();
        Boolean isLock = redisTemplate.opsForValue().setIfAbsent(lockKey, 1, 15L, TimeUnit.SECONDS);
        if (Boolean.TRUE.equals(isLock)) {
            List<Long> anchorIdList = anchorShopInfoService.queryAllValidAnchorId();

            for (Long anchorId : anchorIdList) {
                skuStockInfoRPC.syncStockNumberToMySQL(anchorId);
            }
        }
    }
}