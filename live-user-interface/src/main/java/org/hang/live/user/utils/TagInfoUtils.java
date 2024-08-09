package org.hang.live.user.utils;


/**
 * @Author idea
 * @Date: Created in 17:26 2023/5/27
 * @Description
 */
public class TagInfoUtils {

    /**
     * Check if the user has the tag.
     *
     * @return
     */
    public static boolean isContain(Long tagInfo, Long matchTag) {
        return tagInfo != null && matchTag != null && matchTag > 0 && (tagInfo & matchTag) == matchTag;
    }

}
