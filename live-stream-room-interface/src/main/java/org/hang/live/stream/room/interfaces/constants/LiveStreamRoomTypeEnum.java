package org.hang.live.stream.room.interfaces.constants;

/**
 * @Author hang
 * @Date: Created in 15:49 2024/8/14
 * @Description
 */
public enum LiveStreamRoomTypeEnum {

    DEFAULT_LIVE_STREAM_ROOM(1,"Normal Live Stream Room"),
    PK_LIVE_STREAM_ROOM(2,"PK Live Stream Room");

    LiveStreamRoomTypeEnum(int code, String desc) {
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
