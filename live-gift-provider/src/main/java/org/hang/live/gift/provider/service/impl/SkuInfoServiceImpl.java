package org.hang.live.gift.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.hang.live.common.interfaces.enums.CommonStatusEnum;
import org.hang.live.gift.provider.dao.mapper.ISkuInfoMapper;
import org.hang.live.gift.provider.dao.po.SkuInfoPO;
import org.hang.live.gift.provider.service.ISkuInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/30
 * @Description
 */
@Service
public class SkuInfoServiceImpl implements ISkuInfoService {
    
    @Resource
    private ISkuInfoMapper skuInfoMapper;

    @Override
    public List<SkuInfoPO> queryBySkuIds(List<Long> skuIdList) {
        LambdaQueryWrapper<SkuInfoPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SkuInfoPO::getSkuId, skuIdList);
        queryWrapper.eq(SkuInfoPO::getStatus, CommonStatusEnum.VALID_STATUS.getCode());
        return skuInfoMapper.selectList(queryWrapper);
    }

    @Override
    public SkuInfoPO queryBySkuId(Long skuId) {
        LambdaQueryWrapper<SkuInfoPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SkuInfoPO::getSkuId, skuId);
        queryWrapper.eq(SkuInfoPO::getStatus, CommonStatusEnum.VALID_STATUS.getCode());
        queryWrapper.last("limit 1");
        return skuInfoMapper.selectOne(queryWrapper);
    }
}