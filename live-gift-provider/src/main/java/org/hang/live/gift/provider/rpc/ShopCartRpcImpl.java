package org.hang.live.gift.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.hang.live.gift.dto.ShopCartReqDTO;
import org.hang.live.gift.dto.ShopCartRespDTO;
import org.hang.live.gift.interfaces.IShopCartRPC;
import org.hang.live.gift.provider.service.IShopCartService;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/30
 * @Description
 */
@DubboService
public class ShopCartRpcImpl implements IShopCartRPC {
    
    @Resource
    private IShopCartService shopCartService;

    @Override
    public Boolean addItemToCart(ShopCartReqDTO shopCartReqDTO) {
        return shopCartService.addItemToCart(shopCartReqDTO);
    }

    @Override
    public Boolean removeItemFromCart(ShopCartReqDTO shopCartReqDTO) {
        return shopCartService.removeItemFromCart(shopCartReqDTO);
    }

    @Override
    public Boolean clearCart(ShopCartReqDTO shopCartReqDTO) {
        return shopCartService.clearCart(shopCartReqDTO);
    }

    @Override
    public Boolean increaseItemNumberInCart(ShopCartReqDTO shopCartReqDTO) {
        return shopCartService.increaseItemNumberInCart(shopCartReqDTO);
    }

    @Override
    public ShopCartRespDTO getCartInfo(ShopCartReqDTO shopCartReqDTO) {
        return shopCartService.getCartInfo(shopCartReqDTO);
    }
}