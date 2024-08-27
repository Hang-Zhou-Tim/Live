package org.hang.live.user.interfaces;


import org.hang.live.user.dto.MsgCheckDTO;
import org.hang.live.user.enums.MsgSendResultEnum;

/**
 * @Author idea
 * @Date: Created in 17:21 2023/6/11
 * @Description
 */
public interface ISmsRpc {

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