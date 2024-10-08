package org.hang.live.im.core.server.interfaces.dto;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author hang
 * @Date: Created in 11:06 2024/8/13
 * @Description
 */
public class ImOnlineDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -8966707365668168554L;
    private Long userId;
    private Integer appId;
    private Integer roomId;
    private Long loginTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }

    @Override
    public String toString() {
        return "ImOnlineDTO{" +
                "userId=" + userId +
                ", appId=" + appId +
                ", roomId=" + roomId +
                ", loginTime=" + loginTime +
                '}';
    }
}
