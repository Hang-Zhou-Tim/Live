package org.hang.live.api.vo.req;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/30
 * @Description
 */
public class ShopCartReqVO {
    
    private Long skuId;
    private Integer roomId;

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
        return "ShopCarReqVO{" +
                "skuId=" + skuId +
                ", roomId=" + roomId +
                '}';
    }
}