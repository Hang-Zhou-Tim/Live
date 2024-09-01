package org.hang.live.gift.provider.service.impl;

import jakarta.annotation.Resource;
import org.hang.live.common.redis.configuration.key.GiftProviderCacheKeyBuilder;
import org.hang.live.common.interfaces.utils.ConvertBeanUtils;
import org.hang.live.gift.dto.ShopCartItemRespDTO;
import org.hang.live.gift.dto.ShopCartReqDTO;
import org.hang.live.gift.dto.ShopCartRespDTO;
import org.hang.live.gift.dto.SkuInfoDTO;
import org.hang.live.gift.provider.dao.po.SkuInfoPO;
import org.hang.live.gift.provider.service.IShopCartService;
import org.hang.live.gift.provider.service.ISkuInfoService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/30
 * @Description
 */
@Service
public class ShopCartServiceImpl implements IShopCartService {
    
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private GiftProviderCacheKeyBuilder cacheKeyBuilder;
    @Resource
    private ISkuInfoService skuInfoService;

    /**
     * I used cache to store shopping item in cart
     * The key is based on the room id and user id
     */
    @Override
    public Boolean addItemToCart(ShopCartReqDTO shopCartReqDTO) {
        String cacheKey = cacheKeyBuilder.buildUserShopCar(shopCartReqDTO.getUserId(), shopCartReqDTO.getRoomId());
        redisTemplate.opsForHash().put(cacheKey, String.valueOf(shopCartReqDTO.getSkuId()), 1);
        return true;
    }

    @Override
    public Boolean removeItemFromCart(ShopCartReqDTO shopCartReqDTO) {
        String cacheKey = cacheKeyBuilder.buildUserShopCar(shopCartReqDTO.getUserId(), shopCartReqDTO.getRoomId());
        redisTemplate.opsForHash().delete(cacheKey, String.valueOf(shopCartReqDTO.getSkuId()));
        return true;
    }

    @Override
    public Boolean clearCart(ShopCartReqDTO shopCartReqDTO) {
        String cacheKey = cacheKeyBuilder.buildUserShopCar(shopCartReqDTO.getUserId(), shopCartReqDTO.getRoomId());
        redisTemplate.delete(cacheKey);
        return true;
    }

    @Override
    public Boolean increaseItemNumberInCart(ShopCartReqDTO shopCartReqDTO) {
        String cacheKey = cacheKeyBuilder.buildUserShopCar(shopCartReqDTO.getUserId(), shopCartReqDTO.getRoomId());
        redisTemplate.opsForHash().increment(cacheKey, String.valueOf(shopCartReqDTO.getSkuId()), 1);
        return true;
    }

    @Override
    public ShopCartRespDTO getCartInfo(ShopCartReqDTO shopCartReqDTO) {
        String cacheKey = cacheKeyBuilder.buildUserShopCar(shopCartReqDTO.getUserId(), shopCartReqDTO.getRoomId());
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(cacheKey);
        if (CollectionUtils.isEmpty(entries)) {
            return new ShopCartRespDTO();
        }
        Map<Long, Integer> skuCountMap = new HashMap<>(entries.size());
        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            skuCountMap.put(Long.valueOf((String) entry.getKey()), (Integer) entry.getValue());
        }
        List<Long> skuIdList = new ArrayList<>(skuCountMap.keySet());
        List<SkuInfoPO> skuInfoPOS = skuInfoService.queryBySkuIds(skuIdList);
        ShopCartRespDTO shopCarRespDTO = new ShopCartRespDTO();
        shopCarRespDTO.setRoomId(shopCartReqDTO.getRoomId());
        shopCarRespDTO.setUserId(shopCartReqDTO.getUserId());
        List<ShopCartItemRespDTO> itemList = new ArrayList<>();
        AtomicInteger totalPrice = new AtomicInteger();
        skuInfoPOS.forEach(skuInfoPO -> {
            ShopCartItemRespDTO item = new ShopCartItemRespDTO();
            item.setSkuInfoDTO(ConvertBeanUtils.convert(skuInfoPO, SkuInfoDTO.class));
            item.setCount(skuCountMap.get(skuInfoPO.getSkuId()));
            totalPrice.addAndGet(skuInfoPO.getSkuPrice());
            itemList.add(item);
        });
        shopCarRespDTO.setSkuCarItemRespDTODTOS(itemList);
        shopCarRespDTO.setTotalPrice(totalPrice.get());
        return shopCarRespDTO;
    }

}