package org.hang.live.gift.constants;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/30
 * @Description
 */
public enum SkuOrderInfoEnum {
    PREPARE_PAY(0, "Prepare"),
    HAS_PAY(1, "Payed"),
    CANCEL(2, "Canceled");

    int code;
    String desc;

    SkuOrderInfoEnum(int code, String desc) {
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