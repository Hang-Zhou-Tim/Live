package org.hang.live.api.vo.req;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/21
 * @Description
 */
public class OnlinePkReqVO {

    private Integer roomId;

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return "OnlinePkReqVO{" +
                "roomId=" + roomId +
                '}';
    }
}
