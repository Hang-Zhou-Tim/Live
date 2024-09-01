package org.hang.live.gift.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/30
 * @Description
 */
public class SkuOrderInfoReqDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -9220028624463964600L;
    
    private Long id;
    private Long userId;
    private Integer roomId;
    private Integer status;
    private List<Long> skuIdList;

    public SkuOrderInfoReqDTO(){}

    public SkuOrderInfoReqDTO(Long id, Long userId, Integer roomId, Integer status, List<Long> skuIdList) {
        this.id = id;
        this.userId = userId;
        this.roomId = roomId;
        this.status = status;
        this.skuIdList = skuIdList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<Long> getSkuIdList() {
        return skuIdList;
    }

    public void setSkuIdList(List<Long> skuIdList) {
        this.skuIdList = skuIdList;
    }
}