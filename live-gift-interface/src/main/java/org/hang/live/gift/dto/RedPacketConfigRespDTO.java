package org.hang.live.gift.dto;

import java.io.Serial;
import java.io.Serializable;
/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
public class RedPacketConfigRespDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 6270955403669291409L;
    private Long anchorId;
    private Integer totalPrice;
    private Integer totalCount;
    private String configCode;
    private String remark;

    public Long getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(Long anchorId) {
        this.anchorId = anchorId;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public String getConfigCode() {
        return configCode;
    }

    public void setConfigCode(String configCode) {
        this.configCode = configCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "RedPacketConfigRespDTO{" +
                "anchorId=" + anchorId +
                ", totalPrice=" + totalPrice +
                ", totalCount=" + totalCount +
                ", configCode='" + configCode + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}