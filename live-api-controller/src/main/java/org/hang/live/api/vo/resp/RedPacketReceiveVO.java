package org.hang.live.api.vo.resp;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
public class RedPacketReceiveVO {
    
    private Integer price;
    private String msg;

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "RedPacketReceiveVO{" +
                "price=" + price +
                ", msg='" + msg + '\'' +
                '}';
    }
}