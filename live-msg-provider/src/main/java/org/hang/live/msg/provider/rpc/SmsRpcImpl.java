package org.hang.live.msg.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.hang.live.msg.dto.MsgCheckDTO;
import org.hang.live.msg.enums.MsgSendResultEnum;
import org.hang.live.msg.interfaces.ISmsRpc;
import org.hang.live.msg.provider.service.ISmsService;

/**
 * @Author idea
 * @Date: Created in 17:20 2023/6/11
 * @Description
 */
@DubboService
public class SmsRpcImpl implements ISmsRpc {

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