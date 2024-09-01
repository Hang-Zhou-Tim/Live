package org.hang.live.gift.provider.service;

import org.hang.live.gift.provider.dao.po.SkuInfoPO;

import java.util.List;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/30
 * @Description
 */
public interface ISkuInfoService {

    /**
     * query list of sku info by ids
     */
    List<SkuInfoPO> queryBySkuIds(List<Long> skuIdList);

    /**
     * query sku details by sku id
     */
    SkuInfoPO queryBySkuId(Long skuId);
}