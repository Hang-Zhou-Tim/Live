package org.hang.live.user.payment.constants;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
public enum PayProductTypeEnum {

    COIN(0,"Coin Product for Live Stream App");

    PayProductTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    Integer code;
    String desc;

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
