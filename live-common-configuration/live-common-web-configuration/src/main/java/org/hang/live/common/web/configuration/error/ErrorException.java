package org.hang.live.common.web.configuration.error;

/**
 * @Author hang
 * @Date: Created in 11:11 2024/8/20
 * @Description
 */
public class ErrorException extends RuntimeException{

    private int errorCode;
    private String errorMsg;

    public ErrorException(int errorCode,String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public ErrorException(BaseError BaseError) {
        this.errorCode = BaseError.getErrorCode();
        this.errorMsg = BaseError.getErrorMsg();
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
