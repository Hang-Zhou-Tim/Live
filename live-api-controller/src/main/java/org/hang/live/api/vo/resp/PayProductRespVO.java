package org.hang.live.api.vo.resp;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/24
 * @Description
 */
public class PayProductRespVO {

    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }


    @Override
    public String toString() {
        return "PayProductRespVO{" +
                "orderId='" + orderId + '\'' +
                '}';
    }
}
