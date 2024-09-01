package org.hang.live.gift.provider.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.hang.live.common.interfaces.utils.ConvertBeanUtils;
import org.hang.live.gift.dto.SkuOrderInfoReqDTO;
import org.hang.live.gift.dto.SkuOrderInfoRespDTO;
import org.hang.live.gift.provider.dao.mapper.ISkuOrderInfoMapper;
import org.hang.live.gift.provider.dao.po.SkuOrderInfoPO;
import org.hang.live.gift.provider.service.ISkuOrderInfoService;
import org.springframework.stereotype.Service;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/30
 * @Description
 */
@Service
public class SkuOrderInfoServiceImpl implements ISkuOrderInfoService {
    
    @Resource
    private ISkuOrderInfoMapper skuOrderInfoMapper;

    @Override
    public SkuOrderInfoRespDTO queryByUserIdAndRoomId(Long userId, Integer roomId) {
        LambdaQueryWrapper<SkuOrderInfoPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SkuOrderInfoPO::getUserId, userId);
        queryWrapper.eq(SkuOrderInfoPO::getRoomId, roomId);
        queryWrapper.orderByDesc(SkuOrderInfoPO::getId);
        queryWrapper.last("limit 1");
        return ConvertBeanUtils.convert(skuOrderInfoMapper.selectOne(queryWrapper), SkuOrderInfoRespDTO.class);
    }

    @Override
    public SkuOrderInfoRespDTO queryByOrderId(Long orderId) {
        return ConvertBeanUtils.convert(skuOrderInfoMapper.selectById(orderId), SkuOrderInfoRespDTO.class);
    }

    @Override
    public SkuOrderInfoPO insertOne(SkuOrderInfoReqDTO skuOrderInfoReqDTO) {
        // Use string util to join all sku id into string.
        String skuIdListStr = StrUtil.join(",", skuOrderInfoReqDTO.getSkuIdList());
        SkuOrderInfoPO skuOrderInfoPO = ConvertBeanUtils.convert(skuOrderInfoReqDTO, SkuOrderInfoPO.class);
        skuOrderInfoPO.setSkuIdList(skuIdListStr);
        skuOrderInfoMapper.insert(skuOrderInfoPO);
        return skuOrderInfoPO;
    }

    @Override
    public boolean updateOrderStatus(SkuOrderInfoReqDTO skuOrderInfoReqDTO) {
        SkuOrderInfoPO skuOrderInfoPO = new SkuOrderInfoPO();
        skuOrderInfoPO.setStatus(skuOrderInfoReqDTO.getStatus());
        skuOrderInfoPO.setId(skuOrderInfoReqDTO.getId());
        skuOrderInfoMapper.updateById(skuOrderInfoPO);
        return false;
    }
}