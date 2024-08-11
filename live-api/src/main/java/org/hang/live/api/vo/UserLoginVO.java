package org.hang.live.api.vo;

/**
 * @Author hang
 * @Date: Created in 11:02 2024/8/11
 * @Description
 */
public class UserLoginVO {

    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserLoginVO{" +
                "userId=" + userId +
                '}';
    }
}
