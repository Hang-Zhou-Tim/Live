package org.hang.live.gift.dto;
import java.io.Serial;
import java.io.Serializable;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/30
 * @Description
 */
public class ShopCartItemRespDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1852746764036747357L;
    
    private Integer count;
    private SkuInfoDTO skuInfoDTO;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public SkuInfoDTO getSkuInfoDTO() {
        return skuInfoDTO;
    }

    public void setSkuInfoDTO(SkuInfoDTO skuInfoDTO) {
        this.skuInfoDTO = skuInfoDTO;
    }

    @Override
    public String toString() {
        return "ShopCartItemRespDTO{" +
                "count=" + count +
                ", skuInfoDTO=" + skuInfoDTO +
                '}';
    }
}