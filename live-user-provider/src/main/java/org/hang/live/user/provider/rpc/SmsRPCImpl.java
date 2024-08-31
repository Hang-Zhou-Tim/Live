package org.hang.live.user.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.hang.live.user.dto.MsgCheckDTO;
import org.hang.live.user.enums.MsgSendResultEnum;
import org.hang.live.user.interfaces.ISmsRPC;
import org.hang.live.user.provider.service.ISmsService;


/**
 * @Author hang
 * @Date: Created in 17:20 2024/8/10
 * @Description
 */
@DubboService
public class SmsRPCImpl implements ISmsRPC {

    @Resource
    private ISmsService smsService;

    @Override
    public MsgSendResultEnum sendLoginCode(String phone) {
        return smsService.sendLoginCode(phone);
    }

    @Override
    public MsgCheckDTO checkLoginCode(String phone, Integer code) {
        return smsService.checkLoginCode(phone,code);
    }

}