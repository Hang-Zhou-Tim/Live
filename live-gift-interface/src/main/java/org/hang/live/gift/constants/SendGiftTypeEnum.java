package org.hang.live.gift.constants;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
public enum SendGiftTypeEnum {

    DEFAULT_SEND_GIFT(0,"Default Gift"),
    PK_SEND_GIFT(1,"PK Gift");

    SendGiftTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private Integer code;
    private String desc;

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
