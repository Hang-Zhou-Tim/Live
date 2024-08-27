package org.hang.live.user.enums;

/**
 * @Author hang
 */
public enum MsgSendResultEnum {

    SEND_SUCCESS(0,"Succeed"),
    SEND_FAIL(1,"Failed"),
    MSG_PARAM_ERROR(2,"Parameter Error");

    int code;
    String desc;

    MsgSendResultEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

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