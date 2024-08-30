package org.hang.live.common.web.configuration.config;

import java.lang.annotation.*;

/**
 * @Author hang
 * @Date: Created in 14:04 2023/8/5
 * @Description
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestLimit {

    /**
     * Limited Access Number
     *
     * @return
     */
    int limit();

    /**
     * Limited Range in Second
     *
     * @return
     */
    int second();

    /**
     * Exception Message
     *
     * @return
     */
    String msg() default "Too Request Frequent";
}
