package org.hang.live.im.core.server.interfaces.dto;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author hang
 * @Date: Created in 21:11 2024/8/12
 * @Description
 */

public class ImMsgBody implements Serializable {

    @Serial
    private static final long serialVersionUID = -5799697740854833786L;
    /**
     * The id of application that uses im services.
     */
    private int appId;
    /**
     * User id
     */
    private long userId;
    /**
     * Token id.
     */
    private String token;

    /**
     * Business code.
     */
    private int bizCode;

    /**
     * UUID that represents the message.
     */
    private String msgId;

    /**
     * data used for the business
     */
    private String data;

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getBizCode() {
        return bizCode;
    }

    public void setBizCode(int bizCode) {
        this.bizCode = bizCode;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ImMsgBody{" +
                "appId=" + appId +
                ", userId=" + userId +
                ", token='" + token + '\'' +
                ", msgId='" + msgId + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
