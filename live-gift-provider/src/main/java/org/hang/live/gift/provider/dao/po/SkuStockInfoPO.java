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
@TableName("t_sku_stock_info")
public class SkuStockInfoPO {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Long skuId;
    private Integer stockNum;
    private Integer status;
    private Date createTime;
    private Date updateTime;

    public SkuStockInfoPO(Integer id, Long skuId, Integer stockNum, Integer status, Date createTime, Date updateTime) {
        this.id = id;
        this.skuId = skuId;
        this.stockNum = stockNum;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public SkuStockInfoPO() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Integer getStockNum() {
        return stockNum;
    }

    public void setStockNum(Integer stockNum) {
        this.stockNum = stockNum;
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
}