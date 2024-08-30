package org.hang.live.user.payment.dto;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
public class TransactionTurnoverRespDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 7722131828825334678L;
    private int code;
    private long userId;
    private boolean isSuccess;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static TransactionTurnoverRespDTO buildFail(long userId, String msg,int code) {
        TransactionTurnoverRespDTO tradeRespDTO = new TransactionTurnoverRespDTO();
        tradeRespDTO.setUserId(userId);
        tradeRespDTO.setCode(code);
        tradeRespDTO.setMsg(msg);
        tradeRespDTO.setSuccess(false);
        return tradeRespDTO;
    }

    public static TransactionTurnoverRespDTO buildSuccess(long userId, String msg) {
        TransactionTurnoverRespDTO tradeRespDTO = new TransactionTurnoverRespDTO();
        tradeRespDTO.setUserId(userId);
        tradeRespDTO.setMsg(msg);
        tradeRespDTO.setSuccess(true);
        return tradeRespDTO;
    }

    @Override
    public String toString() {
        return "AccountTradeRespDTO{" +
                "userId=" + userId +
                ", isSuccess=" + isSuccess +
                ", msg='" + msg + '\'' +
                '}';
    }
}
