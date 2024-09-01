package org.hang.live.api.vo.req;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/21
 * @Description
 */
public class SkuInfoReqVO {
    
    private Long skuId;
    private Long anchorId;

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(Long anchorId) {
        this.anchorId = anchorId;
    }

    @Override
    public String toString() {
        return "SkuInfoReqVO{" +
                "skuId=" + skuId +
                ", anchorId=" + anchorId +
                '}';
    }
}