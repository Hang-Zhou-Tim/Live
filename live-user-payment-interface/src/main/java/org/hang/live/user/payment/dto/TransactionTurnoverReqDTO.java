package org.hang.live.user.payment.dto;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
public class TransactionTurnoverReqDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 7722121828825334678L;
    private long userId;
    private int num;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "AccountTradeReqDTO{" +
                "userId=" + userId +
                ", num=" + num +
                '}';
    }
}
