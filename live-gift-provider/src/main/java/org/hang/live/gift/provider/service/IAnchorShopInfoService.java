package org.hang.live.gift.provider.service;

import java.util.List;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/30
 * @Description
 */
public interface IAnchorShopInfoService {

    /**
     * query list of sku Id by anchor Id
     */
    List<Long> querySkuIdsByAnchorId(Long anchorId);

    /**
     * query all anchors who are online and selling products.
     */
    List<Long> queryAllValidAnchorId();
}