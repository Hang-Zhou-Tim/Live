package org.hang.live.web.starter.context;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hang.live.common.interfaces.enums.GatewayHeaderEnum;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.hang.live.web.starter.constants.RequestConstants;
import org.springframework.web.servlet.ModelAndView;

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

