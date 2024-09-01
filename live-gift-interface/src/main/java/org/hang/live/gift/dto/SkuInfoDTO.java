package org.hang.live.gift.dto;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/30
 * @Description
 */
public class SkuInfoDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -8043095169049050521L;
    private Long id;
    private Long skuId;
    private Integer skuPrice;
    private String skuCode;
    private String name;
    private String iconUrl;
    private String originalIconUrl;
    private Integer status;
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Integer getSkuPrice() {
        return skuPrice;
    }

    public void setSkuPrice(Integer skuPrice) {
        this.skuPrice = skuPrice;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getOriginalIconUrl() {
        return originalIconUrl;
    }

    public void setOriginalIconUrl(String originalIconUrl) {
        this.originalIconUrl = originalIconUrl;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "SkuInfoDTO{" +
                "id=" + id +
                ", skuId=" + skuId +
                ", skuPrice=" + skuPrice +
                ", skuCode='" + skuCode + '\'' +
                ", name='" + name + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", originalIconUrl='" + originalIconUrl + '\'' +
                ", status=" + status +
                ", remark='" + remark + '\'' +
                '}';
    }
}