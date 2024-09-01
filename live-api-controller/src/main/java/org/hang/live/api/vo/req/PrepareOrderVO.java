package org.hang.live.api.vo.req;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/30
 * @Description
 */
public class PrepareOrderVO {

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
        return "PrepareOrderVO{" +
                "userId=" + userId +
                ", roomId=" + roomId +
                '}';
    }
}