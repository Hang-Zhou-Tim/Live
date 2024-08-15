package org.hang.live.living.interfaces.dto;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author hang
 * @Date: Created in 15:59 2024/8/14
 * @Description
 */
public class LivingPkRespDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -4135802655494838696L;
    private boolean onlineStatus;
    private String msg;

    public boolean isOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(boolean onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
