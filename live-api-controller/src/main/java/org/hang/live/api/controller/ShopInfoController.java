package org.hang.live.api.controller;

import jakarta.annotation.Resource;
import org.hang.live.api.service.IShopInfoService;
import org.hang.live.api.vo.req.PrepareOrderVO;
import org.hang.live.api.vo.req.ShopCartReqVO;
import org.hang.live.api.vo.req.SkuInfoReqVO;
import org.hang.live.common.interfaces.vo.WebResponseVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/30
 * @Description
 */
@RestController
@RequestMapping("/shop")
public class ShopInfoController {

    @Resource
    private IShopInfoService shopInfoService;

    @PostMapping("/listSkuInfo")
    public WebResponseVO listSkuInfo(Long anchorId) {
        return WebResponseVO.success(shopInfoService.queryByAnchorId(anchorId));
    }

    @PostMapping("/querySkuDetailById")
    public WebResponseVO querySkuDetailById(SkuInfoReqVO reqVO) {
        return WebResponseVO.success(shopInfoService.querySkuDetailById(reqVO));
    }

    @PostMapping("/addItemToCart")
    public WebResponseVO addItemToCart(ShopCartReqVO reqVO) {
        return WebResponseVO.success(shopInfoService.addItemToCart(reqVO));
    }

    @PostMapping("/removeItemFromCart")
    public WebResponseVO removeItemFromCart(ShopCartReqVO reqVO) {
        return WebResponseVO.success(shopInfoService.removeItemFromCart(reqVO));
    }

    @PostMapping("/getCartInfo")
    public WebResponseVO getCartInfo(ShopCartReqVO reqVO) {
        return WebResponseVO.success(shopInfoService.getCartInfo(reqVO));
    }
    
    @PostMapping("/clearCart")
    public WebResponseVO clearCart(ShopCartReqVO reqVO) {
        return WebResponseVO.success(shopInfoService.clearCart(reqVO));
    }
    
    @PostMapping("/prepareOrder")
    public WebResponseVO prepareOrder(PrepareOrderVO prepareOrderVO) {
        return WebResponseVO.success(shopInfoService.prepareOrder(prepareOrderVO));
    }
    
    @PostMapping("/prepareStock")
    public WebResponseVO prepareStock(Long anchorId) {
        return WebResponseVO.success(shopInfoService.prepareStock(anchorId));
    }
    
    @PostMapping("/payNow")
    public WebResponseVO payNow(PrepareOrderVO prepareOrderVO) {
        return WebResponseVO.success(shopInfoService.payNow(prepareOrderVO));
    }
}