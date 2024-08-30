package org.hang.live.common.web.configuration.error;

import jakarta.servlet.http.HttpServletRequest;
import org.hang.live.common.interfaces.vo.WebResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author hang
 * @Date: Created in 11:11 2024/8/20
 * @Description handles global exceptions
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public WebResponseVO errorHandler(HttpServletRequest request, Exception e) {
        LOGGER.error(request.getRequestURI() + ",error is ", e);
        return WebResponseVO.sysError("Sys Error");
    }


    @ExceptionHandler(value = ErrorException.class)
    @ResponseBody
    public WebResponseVO sysErrorHandler(HttpServletRequest request, ErrorException e) {
        //This method handles ErrorExceptions.
        LOGGER.error(request.getRequestURI() + ",error code is {},error msg is {}", e.getErrorCode(), e.getErrorMsg());
        return WebResponseVO.bizError(e.getErrorCode(), e.getErrorMsg());
    }
}
