package org.hang.live.im.router.interfaces.constants;

/**
 * @Author hang
 * @Date: Created in 21:11 2024/8/12
 * @Description
 */
public enum ImMsgBizCodeEnum {

    LIVING_ROOM_IM_CHAT_MSG_BIZ(5555,"live stream room im message"),
    LIVING_ROOM_SEND_GIFT_SUCCESS(5556,"send gifts successfully"),
    LIVING_ROOM_SEND_GIFT_FAIL(5557,"send gifts failed"),
    LIVING_ROOM_PK_SEND_GIFT_SUCCESS(5558,"pk gifts successfully"),
    LIVING_ROOM_PK_ONLINE(5559,"pk live streams started");

    int code;
    String desc;

    ImMsgBizCodeEnum(int code, String desc) {
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
