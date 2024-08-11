package org.hang.live.api.service;

import jakarta.servlet.http.HttpServletResponse;
import org.hang.live.common.interfaces.vo.WebResponseVO;

/**
 * @Author idea
 * @Date: Created in 10:50 2023/6/15
 * @Description
 */
public interface IUserLoginService {

    /**
     * Send Login Code
     *
     * @param phone
     * @return
     */
    WebResponseVO sendLoginCode(String phone);

    /**
     * Phone + Code Loggin
     *
     * @param phone
     * @param code
     * @return
     */
    WebResponseVO login(String phone, Integer code, HttpServletResponse response);
}
