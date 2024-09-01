package org.hang.live.gift.provider.service.bo;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/30
 * @Description
 */
public class RollBackStockBO {
    
    private Long userId;
    private Long orderId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public RollBackStockBO(Long userId, Long orderId) {
        this.userId = userId;
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "RollBackStockBO{" +
                "userId=" + userId +
                ", orderId=" + orderId +
                '}';
    }
}