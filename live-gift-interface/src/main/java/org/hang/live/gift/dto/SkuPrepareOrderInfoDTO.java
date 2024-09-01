package org.hang.live.gift.dto;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/30
 * @Description
 */
public class SkuPrepareOrderInfoDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -2081024866739168825L;
    
    private Integer totalPrice;
    private List<ShopCartItemRespDTO> skuPrepareOrderItemInfoDTOS;

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<ShopCartItemRespDTO> getSkuPrepareOrderItemInfoDTOS() {
        return skuPrepareOrderItemInfoDTOS;
    }

    public void setSkuPrepareOrderItemInfoDTOS(List<ShopCartItemRespDTO> itemList) {
    }
}