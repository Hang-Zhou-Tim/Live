package org.hang.live.common.web.configuration.context;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hang.live.common.interfaces.enums.GatewayHeaderEnum;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.hang.live.common.web.configuration.constants.RequestConstants;
import org.springframework.web.servlet.ModelAndView;
/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/21
 * @Description
 */
//Put user ID into the current thread's context.
public class UserInfoInterceptor implements HandlerInterceptor {

    //All web requests to this entire project will pass through this Interceptor.
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userIdStr = request.getHeader(GatewayHeaderEnum.USER_LOGIN_ID.getName());
        //If the user passes the whitelist so there is empty header
        if (StringUtils.isEmpty(userIdStr)) {
            return true;
        }
        //Put user id to thread local variable in the context.
        RequestContext.set(RequestConstants.HANG_USER_ID, Long.valueOf(userIdStr));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        RequestContext.clear();
    }
}

