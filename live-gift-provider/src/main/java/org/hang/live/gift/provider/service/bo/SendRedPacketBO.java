package org.hang.live.gift.provider.service.bo;

import org.hang.live.gift.dto.RedPacketConfigReqDTO;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description When user snatched the red packet, asynchronisely handle time-consuming DB operation.
 */

public class SendRedPacketBO implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 1829802295999336708L;
    
    private Integer price;
    private RedPacketConfigReqDTO reqDTO;

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public RedPacketConfigReqDTO getReqDTO() {
        return reqDTO;
    }

    public void setReqDTO(RedPacketConfigReqDTO reqDTO) {
        this.reqDTO = reqDTO;
    }

    @Override
    public String toString() {
        return "SendRedPacketBO{" +
                "price=" + price +
                ", reqDTO=" + reqDTO +
                '}';
    }
}