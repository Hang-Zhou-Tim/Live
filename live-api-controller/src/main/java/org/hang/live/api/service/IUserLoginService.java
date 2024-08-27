package org.hang.live.api.service;
import jakarta.servlet.http.HttpServletResponse;
import org.hang.live.common.interfaces.vo.WebResponseVO;

/**
 * @Author hang
 * @Date: Created in 10:50 2024/8/11
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
     * Phone + Code Login
     *
     * @param phone
     * @param code
     * @return
     */
    WebResponseVO login(String phone, Integer code, HttpServletResponse response);
}
