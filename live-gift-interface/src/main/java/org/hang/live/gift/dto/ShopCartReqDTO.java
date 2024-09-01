package org.hang.live.gift.dto;
import java.io.Serial;
import java.io.Serializable;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/30
 * @Description
 */
public class ShopCartReqDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -6322490908161211705L;
    
    private Long userId;
    private Long skuId;
    private Integer roomId;

    public ShopCartReqDTO() {
    }

    public ShopCartReqDTO(Long userId, Long skuId, Integer roomId) {
        this.userId = userId;
        this.skuId = skuId;
        this.roomId = roomId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return "ShopCartReqDTO{" +
                "userId=" + userId +
                ", skuId=" + skuId +
                ", roomId=" + roomId +
                '}';
    }
}