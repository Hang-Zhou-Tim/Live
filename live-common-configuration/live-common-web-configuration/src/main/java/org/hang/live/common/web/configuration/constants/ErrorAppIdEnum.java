package org.hang.live.common.web.configuration.constants;

/**
 * @Author hang
 * @Date: Created in 15:43 2024/8/22
 * @Description
 */
public enum ErrorAppIdEnum {

    API_ERROR(101,"live-api");

    ErrorAppIdEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    int code;
    String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
