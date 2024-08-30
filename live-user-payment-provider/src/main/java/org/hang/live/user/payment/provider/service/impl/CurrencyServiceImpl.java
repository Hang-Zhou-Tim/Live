package org.hang.live.user.payment.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.hang.live.common.redis.configuration.key.BankProviderCacheKeyBuilder;
import org.hang.live.user.payment.dto.CurrencyDTO;
import org.hang.live.user.payment.provider.dao.maper.ICurrencyMapper;
import org.hang.live.user.payment.provider.dao.po.CurrencyPO;
import org.hang.live.common.interfaces.enums.CommonStatusEnum;
import org.hang.live.common.interfaces.utils.ConvertBeanUtils;
import org.hang.live.user.payment.provider.service.ICurrencyService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
@Service
public class CurrencyServiceImpl implements ICurrencyService {

    @Resource
    private ICurrencyMapper currencyMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private BankProviderCacheKeyBuilder cacheKeyBuilder;

    @Override
    public List<CurrencyDTO> getAllCurrencyAmounts(Integer type) {
        String cacheKey = cacheKeyBuilder.buildPayProductCache(type);
        List<CurrencyDTO> cacheList = redisTemplate.opsForList().range(cacheKey, 0, 30).stream().map(x -> {
            return (CurrencyDTO) x;
        }).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(cacheList)) {
            //Return empty list if the cache list is empty object.
            if (cacheList.get(0).getId() == null) {
                return Collections.emptyList();
            }
            return cacheList;
        }
        LambdaQueryWrapper<CurrencyPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CurrencyPO::getType, type);
        queryWrapper.eq(CurrencyPO::getValidStatus, CommonStatusEnum.VALID_STATUS.getCode());
        queryWrapper.orderByDesc(CurrencyPO::getPrice);
        List<CurrencyDTO> currencyDTOS = ConvertBeanUtils.convertList(currencyMapper.selectList(queryWrapper), CurrencyDTO.class);
        if (CollectionUtils.isEmpty(currencyDTOS)) {
            redisTemplate.opsForList().leftPush(cacheKey, new CurrencyDTO());
            redisTemplate.expire(cacheKey, 3, TimeUnit.MINUTES);
            return Collections.emptyList();
        }
        redisTemplate.opsForList().leftPushAll(cacheKey, currencyDTOS.toArray());
        redisTemplate.expire(cacheKey, 30, TimeUnit.MINUTES);
        return currencyDTOS;
    }

    @Override
    public CurrencyDTO getByCurrencyId(Integer productId) {
        String cacheKey = cacheKeyBuilder.buildPayProductItemCache(productId);
        CurrencyDTO currencyDTO = (CurrencyDTO) redisTemplate.opsForValue().get(cacheKey);
        if (currencyDTO != null) {
            if (currencyDTO.getId() == null) {
                return null;
            }
            return currencyDTO;
        }
        CurrencyPO payProductPO = currencyMapper.selectById(productId);
        if (payProductPO != null) {
            CurrencyDTO resultItem = ConvertBeanUtils.convert(payProductPO, CurrencyDTO.class);
            redisTemplate.opsForValue().set(cacheKey, resultItem, 30, TimeUnit.MINUTES);
            return resultItem;
        }
        redisTemplate.opsForValue().set(cacheKey, new CurrencyDTO(), 5, TimeUnit.MINUTES);
        return null;
    }
}
