package org.hang.live.im.constants;

/**
 * @Author hang
 * @Date: Created in 21:11 2024/8/12
 * @Description
 */
public enum AppIdEnum {

    LIVE_BIZ(10001,"Live Application");

    int code;
    String desc;

    AppIdEnum(int code, String desc) {
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
