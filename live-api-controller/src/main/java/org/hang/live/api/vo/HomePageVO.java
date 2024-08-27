package org.hang.live.api.vo;

/**
 * @Author hang
 * @Date: Created in 22:51 2023/8/12
 * @Description
 */
public class HomePageVO {

    private boolean loginStatus;
    private long userId;
    private String nickName;
    private String avatar;
    //Whether the user is an anchor
    private boolean showStartLivingBtn;

    public boolean isShowStartLivingBtn() {
        return showStartLivingBtn;
    }

    public void setShowStartLivingBtn(boolean showStartLivingBtn) {
        this.showStartLivingBtn = showStartLivingBtn;
    }

    public boolean isLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(boolean loginStatus) {
        this.loginStatus = loginStatus;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
