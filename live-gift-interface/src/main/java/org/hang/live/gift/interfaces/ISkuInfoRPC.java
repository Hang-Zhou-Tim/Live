package org.hang.live.gift.interfaces;

import org.hang.live.gift.dto.SkuDetailInfoDTO;
import org.hang.live.gift.dto.SkuInfoDTO;
import java.util.List;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/30
 * @Description
 */
public interface ISkuInfoRPC {

    /**
     * query all product that anchor sells
     */
    List<SkuInfoDTO> queryByAnchorId(Long anchorId);

    /**
     * query details of sku product by sku id
     */
    SkuDetailInfoDTO queryBySkuId(Long skuId, Long anchorId);
}