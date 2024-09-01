package org.hang.live.gift.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.hang.live.common.redis.configuration.key.GiftProviderCacheKeyBuilder;
import org.hang.live.gift.interfaces.ISkuStockInfoRPC;
import org.hang.live.gift.provider.dao.po.SkuStockInfoPO;
import org.hang.live.gift.provider.service.IAnchorShopInfoService;
import org.hang.live.gift.provider.service.ISkuStockInfoService;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/30
 * @Description
 */
@DubboService
public class SkuStockInfoRpcImpl implements ISkuStockInfoRPC {
    
    @Resource
    private ISkuStockInfoService skuStockInfoService;
    @Resource
    private IAnchorShopInfoService anchorShopInfoService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private GiftProviderCacheKeyBuilder cacheKeyBuilder;

    @Override
    public boolean decreaseStockNumberBySkuId(Long skuId, Integer num) {
        return skuStockInfoService.decreaseStockNumberBySkuIdByLua(skuId, num);
    }

    @Override
    public boolean prepareStockInfo(Long anchorId) {
        List<Long> skuIdList = anchorShopInfoService.querySkuIdsByAnchorId(anchorId);
        List<SkuStockInfoPO> skuStockInfoPOS = skuStockInfoService.queryBySkuIds(skuIdList);
        Map<String, Integer> cacheKeyMap = skuStockInfoPOS.stream()
                .collect(Collectors.toMap(skuStockInfoPO -> cacheKeyBuilder.buildSkuStock(skuStockInfoPO.getSkuId()), SkuStockInfoPO::getStockNum));
        redisTemplate.opsForValue().multiSet(cacheKeyMap);
        redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                for (String key : cacheKeyMap.keySet()) {
                    operations.expire((K) key, 1L, TimeUnit.DAYS);
                }
                return null;
            }
        });
        return true;
    }

    @Override
    public Integer queryStockNumber(Long skuId) {
        String cacheKey = cacheKeyBuilder.buildSkuStock(skuId);
        Object stockObj = redisTemplate.opsForValue().get(cacheKey);
        return stockObj == null ? null : (Integer) stockObj;
    }

    @Override
    public boolean syncStockNumberToMySQL(Long anchor) {
        List<Long> skuIdList = anchorShopInfoService.querySkuIdsByAnchorId(anchor);
        for (Long skuId : skuIdList) {
            Integer stockNum = this.queryStockNumber(skuId);
            if (stockNum != null) {
                skuStockInfoService.updateStockNumber(skuId, stockNum);
            }
        }
        return true;
    }
}