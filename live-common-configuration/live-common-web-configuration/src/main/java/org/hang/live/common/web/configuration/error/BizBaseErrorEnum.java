package org.hang.live.common.web.configuration.error;

/**
 * @Author hang
 * @Date: Created in 11:11 2024/8/20
 * @Description
 */
public enum BizBaseErrorEnum implements BaseError{

    PARAM_ERROR(100001,"Parameter Exceptions"),
    TOKEN_ERROR(100002,"User Token Exceptions");

    private int errorCode;
    private String errorMsg;

    BizBaseErrorEnum(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }
}
