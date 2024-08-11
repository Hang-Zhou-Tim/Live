package org.hang.live.common.interfaces.enums;

/**
 * @Author hang
 * @Date: Created in 20:04 2024/8/11
 * @Description
 */
public enum CommonStatusEum {

    INVALID_STATUS(0,"Invalid"),
    VALID_STATUS(1,"Valid");

    CommonStatusEum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    int code;
    String desc;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
