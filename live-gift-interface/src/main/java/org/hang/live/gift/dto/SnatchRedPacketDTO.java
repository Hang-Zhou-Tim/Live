package org.hang.live.gift.dto;


import java.io.Serial;
import java.io.Serializable;
/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
public class SnatchRedPacketDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -8807970783603506957L;
    
    private Integer price;
    private String notifyMsg;

    public SnatchRedPacketDTO(Integer price, String notifyMsg) {
        this.price = price;
        this.notifyMsg = notifyMsg;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getNotifyMsg() {
        return notifyMsg;
    }

    public void setNotifyMsg(String notifyMsg) {
        this.notifyMsg = notifyMsg;
    }

    @Override
    public String toString() {
        return "RedPacketReceiveDTO{" +
                "price=" + price +
                ", notifyMsg='" + notifyMsg + '\'' +
                '}';
    }
}