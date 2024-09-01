package org.hang.live.gift.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.hang.live.common.redis.configuration.key.GiftProviderCacheKeyBuilder;
import org.hang.live.user.payment.dto.CurrencyDTO;
import org.hang.live.common.interfaces.utils.ConvertBeanUtils;
import org.hang.live.gift.dto.SkuDetailInfoDTO;
import org.hang.live.gift.dto.SkuInfoDTO;
import org.hang.live.gift.interfaces.ISkuInfoRPC;
import org.hang.live.gift.provider.service.IAnchorShopInfoService;
import org.hang.live.gift.provider.service.ISkuInfoService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
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
public class SkuInfoRpcImpl implements ISkuInfoRPC {

    @Resource
    private ISkuInfoService skuInfoService;
    @Resource
    private IAnchorShopInfoService anchorShopInfoService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private GiftProviderCacheKeyBuilder cacheKeyBuilder;

    @Override
    public List<SkuInfoDTO> queryByAnchorId(Long anchorId) {
        //Get all sku product that anchor sells in Redis.
        String cacheKey = cacheKeyBuilder.buildSkuDetailInfoMap(anchorId);
        List<SkuInfoDTO> skuInfoDTOS = redisTemplate.opsForHash().values(cacheKey).stream().map(x -> (SkuInfoDTO) x).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(skuInfoDTOS)) {
            //If redis has these products' record, return them. Else if redis has records with empty object, return empty list.
            if (skuInfoDTOS.get(0).getSkuId() == null) {
                return Collections.emptyList();
            }
            return skuInfoDTOS;
        }
        //Get all sku product that anchor sells in DB if Redis does not have record.
        List<Long> skuIdList = anchorShopInfoService.querySkuIdsByAnchorId(anchorId);
        if (CollectionUtils.isEmpty(skuIdList)) {
            return Collections.emptyList();
        }
        skuInfoDTOS = ConvertBeanUtils.convertList(skuInfoService.queryBySkuIds(skuIdList), SkuInfoDTO.class);
        //If DB has no such record, put result of this query as empty list to avoid duplicate query to query DB.
        if (CollectionUtils.isEmpty(skuInfoDTOS)) {
            redisTemplate.opsForHash().put(cacheKey, -1, new CurrencyDTO());
            redisTemplate.expire(cacheKey, 1L, TimeUnit.MINUTES);
            return Collections.emptyList();
        }
        //Cache the db result into redis.
        Map<String, SkuInfoDTO> skuInfoMap = skuInfoDTOS.stream().collect(Collectors.toMap(x -> String.valueOf(x.getSkuId()), x -> x));
        redisTemplate.opsForHash().putAll(cacheKey, skuInfoMap);
        redisTemplate.expire(cacheKey, 30L, TimeUnit.MINUTES);
        return skuInfoDTOS;
    }

    @Override
    public SkuDetailInfoDTO queryBySkuId(Long skuId, Long anchorId) {
        String cacheKey = cacheKeyBuilder.buildSkuDetailInfoMap(anchorId);
        SkuInfoDTO skuInfoDTO = (SkuInfoDTO) redisTemplate.opsForHash().get(cacheKey, String.valueOf(skuId));
        if (skuInfoDTO != null) {
            return ConvertBeanUtils.convert(skuInfoDTO, SkuDetailInfoDTO.class);
        }
        skuInfoDTO = ConvertBeanUtils.convert(skuInfoService.queryBySkuId(skuId), SkuInfoDTO.class);
        if (skuInfoDTO != null) {
            redisTemplate.opsForHash().put(cacheKey, String.valueOf(skuId), skuInfoDTO);
        }
        return ConvertBeanUtils.convert(skuInfoDTO, SkuDetailInfoDTO.class);
    }
}