package org.hang.live.gift.provider.service;

import org.hang.live.gift.provider.service.bo.RollBackStockBO;
import org.hang.live.gift.provider.dao.po.SkuStockInfoPO;

import java.util.List;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/30
 * @Description
 */
public interface ISkuStockInfoService {

    /**
     * update stock number of sku by sku id
     */
    boolean updateStockNumber(Long skuId, Integer stockNum);

    /**
     * decrease stock number by sku id
     */
    boolean decreaseStockNumberBySkuId(Long skuId, Integer num);

    /**
     * decrease stock number by sku id using lua to allow atomic operation
     */
    boolean decreaseStockNumberBySkuIdByLua(Long skuId, Integer num);

    /**
     * query stock number by sku Id
     */
    SkuStockInfoPO queryBySkuId(Long skuId);

    /**
     * query product info by list of sku id
     */
    List<SkuStockInfoPO> queryBySkuIds(List<Long> skuIdList);

    /**
     * rollback stock if user does not pay order in limited time
     */
    void stockRollBackHandler(RollBackStockBO rollBackStockBO);

}