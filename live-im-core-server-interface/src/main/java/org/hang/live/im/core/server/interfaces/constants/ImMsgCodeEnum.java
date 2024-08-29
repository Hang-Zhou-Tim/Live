package org.hang.live.im.core.server.interfaces.constants;

/**
 * @Author hang
 * @Date: Created in 20:36 2024/8/12
 * @Description
 */
public enum ImMsgCodeEnum {

    IM_LOGIN_MSG(1001,"Login IM Message Packet"),
    IM_LOGOUT_MSG(1002,"Logout IM Message Packet"),
    IM_BIZ_MSG(1003,"Business IM Message Packet"),
    IM_HEARTBEAT_MSG(1004,"Heart Beat IM Message Packet"),
    IM_ACK_MSG(1005,"ACK IM Message Packet");

    private int code;
    private String desc;

    ImMsgCodeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
