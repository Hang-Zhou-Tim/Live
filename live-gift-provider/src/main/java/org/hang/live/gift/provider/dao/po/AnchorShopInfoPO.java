package org.hang.live.gift.provider.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/30
 * @Description
 */
@TableName("t_anchor_shop_info")
public class AnchorShopInfoPO {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Long anchorId;
    private Long skuId;
    private Integer status;
    private Date createTime;
    private Date updateTime;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(Long anchorId) {
        this.anchorId = anchorId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
    public AnchorShopInfoPO(){};
    public AnchorShopInfoPO(Integer id, Long anchorId, Long skuId, Integer status, Date createTime, Date updateTime) {
        this.id = id;
        this.anchorId = anchorId;
        this.skuId = skuId;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "AnchorShopInfoPO{" +
                "id=" + id +
                ", anchorId=" + anchorId +
                ", skuId=" + skuId +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}