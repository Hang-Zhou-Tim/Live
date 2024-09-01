package org.hang.live.gift.dto;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/30
 * @Description
 */
public class ShopCartRespDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -7490295225808797655L;
    
    private Long userId;
    private Integer roomId;
    private List<ShopCartItemRespDTO> skuCartItemRespDTOS;

    private Integer totalPrice;

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

    public List<ShopCartItemRespDTO> getSkuCarItemRespDTOS() {
        return skuCartItemRespDTOS;
    }

    public void setSkuCarItemRespDTODTOS(List<ShopCartItemRespDTO> skuCartItemRespDTODTOS) {
        this.skuCartItemRespDTOS = skuCartItemRespDTODTOS;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setSkuCartItemRespDTOS(List<ShopCartItemRespDTO> skuCartItemRespDTOS) {
        this.skuCartItemRespDTOS = skuCartItemRespDTOS;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public List<ShopCartItemRespDTO> getSkuCartItemRespDTOS() {
        return skuCartItemRespDTOS;
    }


    @Override
    public String toString() {
        return "ShopCartRespDTO{" +
                "userId=" + userId +
                ", roomId=" + roomId +
                ", skuCarItemRespDTOS=" + skuCartItemRespDTOS +
                '}';
    }
}