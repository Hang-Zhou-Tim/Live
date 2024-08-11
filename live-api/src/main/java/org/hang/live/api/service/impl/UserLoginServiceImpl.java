package org.hang.live.api.service.impl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.dubbo.config.annotation.DubboReference;
import org.hang.live.account.interfaces.IAccountTokenRPC;
import org.hang.live.api.service.IUserLoginService;
import org.hang.live.api.vo.UserLoginVO;
import org.hang.live.common.interfaces.utils.ConvertBeanUtils;
import org.hang.live.common.interfaces.vo.WebResponseVO;
import org.hang.live.msg.dto.MsgCheckDTO;
import org.hang.live.msg.enums.MsgSendResultEnum;
import org.hang.live.msg.interfaces.ISmsRpc;
import org.hang.live.user.dto.UserLoginDTO;
import org.hang.live.user.interfaces.IUserPhoneRPC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

/**
 * @Author idea
 * @Date: Created in 10:51 2023/6/15
 * @Description
 */
@Service
public class UserLoginServiceImpl implements IUserLoginService {

    private static String PHONE_REG = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$";
    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoginServiceImpl.class);

    @DubboReference
    private ISmsRpc smsRpc;
    @DubboReference
    private IUserPhoneRPC userPhoneRPC;
    @DubboReference
    private IAccountTokenRPC accountTokenRPC;

    @Override
    public WebResponseVO sendLoginCode(String phone) {
//        ErrorAssert.isNotBlank(phone, ApiErrorEnum.PHONE_IS_EMPTY);
//        ErrorAssert.isTure(Pattern.matches(PHONE_REG, phone), ApiErrorEnum.PHONE_IN_VALID);
        MsgSendResultEnum msgSendResultEnum = smsRpc.sendLoginCode(phone);
        if (msgSendResultEnum == MsgSendResultEnum.SEND_SUCCESS) {
            return WebResponseVO.success();
        }
        return WebResponseVO.sysError("Sending too many requests, try it later.");
    }

    @Override
    public WebResponseVO login(String phone, Integer code, HttpServletResponse response) {
        MsgCheckDTO msgCheckDTO = smsRpc.checkLoginCode(phone, code);
        if (!msgCheckDTO.isCheckStatus()) {
            return WebResponseVO.bizError(msgCheckDTO.getDesc());
        }
        //Validation Code Passes
        UserLoginDTO userLoginDTO = userPhoneRPC.login(phone);

        String token = accountTokenRPC.createAndSaveLoginToken(userLoginDTO.getUserId());
        Cookie cookie = new Cookie("hlivetoken", token);
        //http://app.qiyu.live.com/html/qiyu_live_list_room.html
        //http://api.qiyu.live.com/live/api/userLogin/sendLoginCode
        cookie.setDomain("hang.live.com");
        cookie.setPath("/");
        //cookie expiration date with Second Unit
        cookie.setMaxAge(30 * 24 * 3600);
        //Add this to response otherwise cookie is not valid
        response.addCookie(cookie);
        return WebResponseVO.success(ConvertBeanUtils.convert(userLoginDTO, UserLoginVO.class));
    }
}
