package org.hang.live.gift.interfaces;

import org.hang.live.gift.dto.ShopCartReqDTO;
import org.hang.live.gift.dto.ShopCartRespDTO;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/30
 * @Description
 */
public interface IShopCartRPC {

    /**
     * add item to cart
     */
    Boolean addItemToCart(ShopCartReqDTO shopCartReqDTO);

    /**
     * remove item to cart
     */
    Boolean removeItemFromCart(ShopCartReqDTO shopCartReqDTO);

    /**
     * clear cart
     */
    Boolean clearCart(ShopCartReqDTO shopCartReqDTO);

    /**
     * increase amount of item in cart
     */
    Boolean increaseItemNumberInCart(ShopCartReqDTO shopCartReqDTO);

    /**
     * check cart info
     */
    ShopCartRespDTO getCartInfo(ShopCartReqDTO shopCartReqDTO);
}