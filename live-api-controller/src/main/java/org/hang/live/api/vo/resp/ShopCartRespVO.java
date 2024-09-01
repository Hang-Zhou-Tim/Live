package org.hang.live.api.vo.resp;


import org.hang.live.gift.dto.ShopCartItemRespDTO;

import java.util.List;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/30
 * @Description
 */
public class ShopCartRespVO {

    private Long userId;
    private Integer roomId;

    private Integer totalPrice;
    private List<ShopCartItemRespDTO> shopCartItemRespDTOS;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public List<ShopCartItemRespDTO> getShopCartItemRespDTOS() {
        return shopCartItemRespDTOS;
    }


    @Override
    public String toString() {
        return "ShopCarRespVO{" +
                "userId=" + userId +
                ", roomId=" + roomId +
                ", shopCartItemRespDTOS=" + shopCartItemRespDTOS +
                '}';
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setShopCartItemRespDTOS(List<ShopCartItemRespDTO> shopCartItemRespDTOS) {
        this.shopCartItemRespDTOS = shopCartItemRespDTOS;
    }
}