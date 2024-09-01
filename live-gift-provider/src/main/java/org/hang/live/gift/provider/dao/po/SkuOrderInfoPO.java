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
@TableName("t_sku_order_info")
public class SkuOrderInfoPO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String skuIdList;
    private Long userId;
    private Integer roomId;
    private Integer status;
    private String extra;
    private Date createTime;
    private Date updateTime;

    public SkuOrderInfoPO(Long id, String skuIdList, Long userId, Integer roomId, Integer status, String extra, Date createTime, Date updateTime) {
        this.id = id;
        this.skuIdList = skuIdList;
        this.userId = userId;
        this.roomId = roomId;
        this.status = status;
        this.extra = extra;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public SkuOrderInfoPO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSkuIdList() {
        return skuIdList;
    }

    public void setSkuIdList(String skuIdList) {
        this.skuIdList = skuIdList;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
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