package org.hang.live.user.provider.service;
import org.hang.live.user.dto.MsgCheckDTO;
import org.hang.live.user.enums.MsgSendResultEnum;

/**
 * @Author hang
 * @Date: Created in 17:20 2024/8/10
 * @Description
 */
public interface ISmsService {

    /**
     * Interface to send message
     *
     * @param phone
     * @return
     */
    MsgSendResultEnum sendLoginCode(String phone);

    /**
     * Validate Login Message
     *
     * @param phone
     * @param code
     * @return
     */
    MsgCheckDTO checkLoginCode(String phone, Integer code);

    /**
     * Insert SMS Message
     *
     * @param phone
     * @param code
     */
    void insertOne(String phone, Integer code);

}