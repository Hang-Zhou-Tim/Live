package org.hang.live.user.payment.constants;


/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
public enum PaySourceEnum {

    LIVING_ROOM(1,"live stream room"),
    USER_CENTER(2,"user center");

    PaySourceEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static PaySourceEnum find(int code) {
        for (PaySourceEnum value : PaySourceEnum.values()) {
            if(value.getCode() == code) {
                return value;
            }
        }
        return null;
    }

    private int code;
    private String desc;

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
