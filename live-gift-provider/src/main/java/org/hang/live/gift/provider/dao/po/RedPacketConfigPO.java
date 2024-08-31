package org.hang.live.gift.provider.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;
import java.util.Objects;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
@TableName("t_red_packet_config")
public class RedPacketConfigPO {
    
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Long anchorId;
    private Date startTime;
    private Integer totalGet;
    private Integer totalGetPrice;
    private Integer maxGetPrice;
    private Integer status;
    private Integer totalPrice;
    private Integer totalCount;
    private String configCode;
    private String remark;
    private Date createTime;
    private Date updateTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RedPacketConfigPO that = (RedPacketConfigPO) o;
        return Objects.equals(id, that.id) && Objects.equals(anchorId, that.anchorId) && Objects.equals(startTime, that.startTime) && Objects.equals(totalGet, that.totalGet) && Objects.equals(totalGetPrice, that.totalGetPrice) && Objects.equals(maxGetPrice, that.maxGetPrice) && Objects.equals(status, that.status) && Objects.equals(totalPrice, that.totalPrice) && Objects.equals(totalCount, that.totalCount) && Objects.equals(configCode, that.configCode) && Objects.equals(remark, that.remark) && Objects.equals(createTime, that.createTime) && Objects.equals(updateTime, that.updateTime);
    }

    public Long getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(Long anchorId) {
        this.anchorId = anchorId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "RedPacketConfigPO{" +
                "id=" + id +
                ", anchorId=" + anchorId +
                ", startTime=" + startTime +
                ", totalGet=" + totalGet +
                ", totalGetPrice=" + totalGetPrice +
                ", maxGetPrice=" + maxGetPrice +
                ", status=" + status +
                ", totalPrice=" + totalPrice +
                ", totalCount=" + totalCount +
                ", configCode='" + configCode + '\'' +
                ", remark='" + remark + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, anchorId, startTime, totalGet, totalGetPrice, maxGetPrice, status, totalPrice, totalCount, configCode, remark, createTime, updateTime);
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Integer getTotalGet() {
        return totalGet;
    }

    public void setTotalGet(Integer totalGet) {
        this.totalGet = totalGet;
    }

    public Integer getTotalGetPrice() {
        return totalGetPrice;
    }

    public void setTotalGetPrice(Integer totalGetPrice) {
        this.totalGetPrice = totalGetPrice;
    }

    public Integer getMaxGetPrice() {
        return maxGetPrice;
    }

    public void setMaxGetPrice(Integer maxGetPrice) {
        this.maxGetPrice = maxGetPrice;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}