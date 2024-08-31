package org.hang.live.gift.dto;

import java.io.Serial;
import java.io.Serializable;
/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
public class RedPacketConfigReqDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 3064212465011493178L;
    private Integer id;
    private Integer roomId;
    private Integer status;
    private Long userId;
    private String redPacketConfigCode;
    private Integer totalPrice;
    private Integer totalCount;
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRedPacketConfigCode() {
        return redPacketConfigCode;
    }

    public void setRedPacketConfigCode(String redPacketConfigCode) {
        this.redPacketConfigCode = redPacketConfigCode;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "RedPacketConfigReqDTO{" +
                "id=" + id +
                ", roomId=" + roomId +
                ", status=" + status +
                ", userId=" + userId +
                ", redPacketConfigCode='" + redPacketConfigCode + '\'' +
                ", totalPrice=" + totalPrice +
                ", totalCount=" + totalCount +
                ", remark='" + remark + '\'' +
                '}';
    }
}