package org.hang.live.gift.constants;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
public enum RedPacketStatusEnum {
    
    WAIT(1,"prepare"),
    IS_PREPARED(2, "ready"),
    IS_SEND(3, "sent");

    int code;
    String desc;

	RedPacketStatusEnum(int code, String desc) {
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