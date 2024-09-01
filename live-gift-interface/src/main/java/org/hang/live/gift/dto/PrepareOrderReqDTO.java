package org.hang.live.gift.dto;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/30
 * @Description
 */
public class PrepareOrderReqDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -5436121173173601521L;
    
    private Long userId;
    private Integer roomId;

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

    @Override
    public String toString() {
        return "PrepareOrderReqDTO{" +
                "userId=" + userId +
                ", roomId=" + roomId +
                '}';
    }
}