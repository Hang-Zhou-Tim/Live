package org.hang.live.gift.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import jakarta.annotation.Resource;
import org.hang.live.common.redis.configuration.key.GiftProviderCacheKeyBuilder;
import org.hang.live.common.interfaces.enums.CommonStatusEnum;
import org.hang.live.gift.provider.service.bo.RollBackStockBO;
import org.hang.live.gift.constants.SkuOrderInfoEnum;
import org.hang.live.gift.dto.SkuOrderInfoReqDTO;
import org.hang.live.gift.dto.SkuOrderInfoRespDTO;
import org.hang.live.gift.provider.dao.mapper.ISkuStockInfoMapper;
import org.hang.live.gift.provider.dao.po.SkuStockInfoPO;
import org.hang.live.gift.provider.service.ISkuOrderInfoService;
import org.hang.live.gift.provider.service.ISkuStockInfoService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/30
 * @Description
 */
@Service
public class SkuStockInfoServiceImpl implements ISkuStockInfoService {

    @Resource
    private ISkuStockInfoMapper skuStockInfoMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private GiftProviderCacheKeyBuilder cacheKeyBuilder;
    @Resource
    private ISkuOrderInfoService skuOrderInfoService;
    private static final String SECKILL_SCRIPT =
            "if (redis.call('exists', KEYS[1])) == 1 then " +
            " local currentStock = redis.call('get', KEYS[1]) " +
            " if (tonumber(currentStock) >= tonumber(ARGV[1])) then " +
            " return redis.call('decrby', KEYS[1], tonumber(ARGV[1])) " +
            " else return -1 end" +
            " return -1 end";

    @Override
    public boolean decreaseStockNumberBySkuIdByLua(Long skuId, Integer num) {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript();
        redisScript.setScriptText(SECKILL_SCRIPT);
        redisScript.setResultType(Long.class);
        return redisTemplate.execute(
                redisScript,
                Collections.singletonList(cacheKeyBuilder.buildSkuStock(skuId)),
                num
        ) >= 0;
    }

    @Override
    public boolean updateStockNumber(Long skuId, Integer stockNum) {
        LambdaUpdateWrapper<SkuStockInfoPO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SkuStockInfoPO::getSkuId, skuId);
        SkuStockInfoPO skuStockInfoPO = new SkuStockInfoPO();
        skuStockInfoPO.setStockNum(stockNum);
        return skuStockInfoMapper.update(skuStockInfoPO, updateWrapper) > 0;
    }

    @Override
    public boolean decreaseStockNumberBySkuId(Long skuId, Integer num) {
        return skuStockInfoMapper.decrStockNumBySkuId(skuId, num);
    }

    @Override
    public SkuStockInfoPO queryBySkuId(Long skuId) {
        LambdaQueryWrapper<SkuStockInfoPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SkuStockInfoPO::getSkuId, skuId);
        queryWrapper.eq(SkuStockInfoPO::getStatus, CommonStatusEnum.VALID_STATUS.getCode());
        queryWrapper.last("limit 1");
        return skuStockInfoMapper.selectOne(queryWrapper);
    }

    @Override
    public List<SkuStockInfoPO> queryBySkuIds(List<Long> skuIdList) {
        LambdaQueryWrapper<SkuStockInfoPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SkuStockInfoPO::getSkuId, skuIdList);
        queryWrapper.eq(SkuStockInfoPO::getStatus, CommonStatusEnum.VALID_STATUS.getCode());
        return skuStockInfoMapper.selectList(queryWrapper);
    }

    @Override
    public void stockRollBackHandler(RollBackStockBO rollBackStockBO) {
        SkuOrderInfoRespDTO respDTO = skuOrderInfoService.queryByOrderId(rollBackStockBO.getOrderId());
        if (respDTO == null || !respDTO.getStatus().equals(SkuOrderInfoEnum.PREPARE_PAY.getCode())) {
            return;
        }
        SkuOrderInfoReqDTO skuOrderInfoReqDTO = new SkuOrderInfoReqDTO();
        skuOrderInfoReqDTO.setStatus(SkuOrderInfoEnum.CANCEL.getCode());
        skuOrderInfoReqDTO.setId(rollBackStockBO.getOrderId());
        // set the status to "Cancel"
        skuOrderInfoService.updateOrderStatus(skuOrderInfoReqDTO);
        // roll back the stock for each sku id in order
        List<Long> skuIdList = Arrays.stream(respDTO.getSkuIdList().split(",")).map(Long::valueOf).collect(Collectors.toList());
        skuIdList.parallelStream().forEach(skuId -> {
            // Concurrently increment sku stock.
            redisTemplate.opsForValue().increment(cacheKeyBuilder.buildSkuStock(skuId), 1);
        });
    }
}