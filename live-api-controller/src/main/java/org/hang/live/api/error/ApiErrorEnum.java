package org.hang.live.api.error;

import org.hang.live.common.web.configuration.error.BaseError;

public enum ApiErrorEnum implements BaseError {

    PHONE_IS_EMPTY(1, "Phone number shouldn't be null."),
    PHONE_IN_VALID(2,"Invalid phone number format."),
    SMS_CODE_ERROR(3,"Validation code error"),
    USER_LOGIN_ERROR(4,"Login failed"),
    GIFT_CONFIG_ERROR(5,"Invalid gift config"),
    SEND_GIFT_ERROR(6,"Sending gift failed"),
    PK_ONLINE_BUSY(7,"There is someone else connected to PK room. Retry later"),
    NOT_SEND_TO_YOURSELF(8,"Cannot send gift to yourself"),
    LIVING_ROOM_END(9,"Live stream room ended");


    ApiErrorEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    int code;
    String desc;


    @Override
    public int getErrorCode() {
        return code;
    }

    @Override
    public String getErrorMsg() {
        return desc;
    }
}