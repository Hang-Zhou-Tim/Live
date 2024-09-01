package org.hang.live.api.service;

import org.hang.live.api.vo.req.PrepareOrderVO;
import org.hang.live.api.vo.req.ShopCartReqVO;
import org.hang.live.api.vo.req.SkuInfoReqVO;
import org.hang.live.api.vo.resp.ShopCartRespVO;
import org.hang.live.api.vo.resp.SkuDetailInfoVO;
import org.hang.live.api.vo.resp.SkuInfoVO;
import org.hang.live.gift.dto.SkuPrepareOrderInfoDTO;

import java.util.List;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/30
 * @Description
 */
public interface IShopInfoService {

    /**
     * query all products that the anchor sells
     */
    List<SkuInfoVO> queryByAnchorId(Long anchorId);

    /**
     * query details of products based on id.
     */
    SkuDetailInfoVO querySkuDetailById(SkuInfoReqVO skuInfoReqVO);
    /**
     * Add Item to Shopping Cart
     */
    Boolean addItemToCart(ShopCartReqVO reqVO);

    /**
     * Remove Item From Shopping Cart
     */
    Boolean removeItemFromCart(ShopCartReqVO reqVO);

    /**
     * Clear Item From Shopping Cart
     */
    Boolean clearCart(ShopCartReqVO reqVO);

    /**
     * Change item number in shopping cart
     */
    Boolean increaseItemNumberInCart(ShopCartReqVO reqVO);

    /**
     * View Shopping Cart Details
     */
    ShopCartRespVO getCartInfo(ShopCartReqVO reqVO);

    /**
     * prepare order
     */
    SkuPrepareOrderInfoDTO prepareOrder(PrepareOrderVO prepareOrderVO);

    /**
     * prepare stocks to Redis
     */
    boolean prepareStock(Long anchorId);

    /**
     * pay the order now
     */
    boolean payNow(PrepareOrderVO prepareOrderVO);
}