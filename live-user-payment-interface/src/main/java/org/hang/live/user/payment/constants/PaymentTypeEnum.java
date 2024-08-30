package org.hang.live.user.payment.constants;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
public enum PaymentTypeEnum {

    SEND_GIFT(0,"Send Gift Payment"),
    LIVE_STREAM_ROOM_RECHARGE(1,"Live Stream Room Payment");

    int code;
    String desc;

    PaymentTypeEnum(int code, String desc) {
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
