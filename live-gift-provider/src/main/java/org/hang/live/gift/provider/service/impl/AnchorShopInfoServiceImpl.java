package org.hang.live.gift.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.hang.live.common.interfaces.enums.CommonStatusEnum;
import org.hang.live.gift.provider.dao.mapper.IAnchorShopInfoMapper;
import org.hang.live.gift.provider.dao.po.AnchorShopInfoPO;
import org.hang.live.gift.provider.service.IAnchorShopInfoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/30
 * @Description
 */

@Service
public class AnchorShopInfoServiceImpl implements IAnchorShopInfoService {
    
    @Resource
    private IAnchorShopInfoMapper anchorShopInfoMapper;
    
    @Override
    public List<Long> querySkuIdsByAnchorId(Long anchorId) {
        LambdaQueryWrapper<AnchorShopInfoPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AnchorShopInfoPO::getAnchorId, anchorId);
        //In Test Environment, do not check status for the goods.
        //queryWrapper.eq(AnchorShopInfoPO::getStatus, CommonStatusEnum.VALID_STATUS.getCode());

        queryWrapper.select(AnchorShopInfoPO::getSkuId);
        List<AnchorShopInfoPO> res = anchorShopInfoMapper.selectList(queryWrapper);
        return res == null? null : res.stream().map(AnchorShopInfoPO::getSkuId).collect(Collectors.toList());
    }

    @Override
    public List<Long> queryAllValidAnchorId() {
        LambdaQueryWrapper<AnchorShopInfoPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AnchorShopInfoPO::getStatus, CommonStatusEnum.VALID_STATUS.getCode());
        return anchorShopInfoMapper.selectList(queryWrapper).stream().map(AnchorShopInfoPO::getAnchorId).collect(Collectors.toList());
    }
}