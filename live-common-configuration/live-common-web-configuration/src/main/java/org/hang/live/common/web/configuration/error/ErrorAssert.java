package org.hang.live.common.web.configuration.error;


/**
 * @Author hang
 * @Date: Created in 11:11 2024/8/20
 * @Description
 */
public class ErrorAssert {


    /**
     * Check if parameter is not null
     *
     * @param obj
     * @param BaseError
     */
    public static void isNotNull(Object obj, BaseError BaseError) {
        if (obj == null) {
            throw new ErrorException(BaseError);
        }
    }

    /**
     * Check if the string is empty
     *
     * @param str
     * @param BaseError
     */
    public static void isNotBlank(String str, BaseError BaseError) {
        if (str == null || str.trim().length() == 0) {
            throw new ErrorException(BaseError);
        }
    }

    /**
     * check if flag == true
     *
     * @param flag
     * @param BaseError
     */
    public static void isTure(boolean flag, BaseError BaseError) {
        if (!flag) {
            throw new ErrorException(BaseError);
        }
    }

    /**
     * check if flag == true
     *
     * @param flag
     * @param ErrorException
     */
    public static void isTure(boolean flag, ErrorException ErrorException) {
        if (!flag) {
            throw ErrorException;
        }
    }
}
