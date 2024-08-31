package org.hang.live.user.interfaces;


import org.hang.live.user.dto.MsgCheckDTO;
import org.hang.live.user.enums.MsgSendResultEnum;

/**
 * @Author hang
 * @Date: Created in 10:13 2024/8/10
 * @Description
 */
public interface ISmsRPC {

    /**
     * Send SMS message
     *
     * @param phone
     * @return
     */
    MsgSendResultEnum sendLoginCode(String phone);

    /**
     * Validate Login message
     *
     * @param phone
     * @param code
     * @return
     */
    MsgCheckDTO checkLoginCode(String phone, Integer code);

}