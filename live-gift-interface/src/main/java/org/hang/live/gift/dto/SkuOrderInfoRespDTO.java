package org.hang.live.gift.dto;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/30
 * @Description
 */
public class SkuOrderInfoRespDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 3949272249650947118L;

    private Long Id;
    private String skuIdList;
    private Long userId;
    private Integer roomId;
    private Integer status;
    private String extra;

    public SkuOrderInfoRespDTO() {
    }

    public SkuOrderInfoRespDTO(Long id, String skuIdList, Long userId, Integer roomId, Integer status, String extra) {
        Id = id;
        this.skuIdList = skuIdList;
        this.userId = userId;
        this.roomId = roomId;
        this.status = status;
        this.extra = extra;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
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

    @Override
    public String toString() {
        return "SkuOrderInfoRespDTO{" +
                "Id=" + Id +
                ", skuIdList='" + skuIdList + '\'' +
                ", userId=" + userId +
                ", roomId=" + roomId +
                ", status=" + status +
                ", extra='" + extra + '\'' +
                '}';
    }
}