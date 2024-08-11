package org.hang.live.user.provider.service;

import org.hang.live.user.constants.UserTagsEnum;

/**
 * @Author hang
 * @Date: Created in 17:12 2024/8/11
 * @Description
 */
public interface IUserTagService {

    /**
     * Set the tag
     *
     * @param userId
     * @param userTagsEnum
     * @return
     */
    boolean setTag(Long userId, UserTagsEnum userTagsEnum);

    /**
     * Cancel the tag
     *
     * @param userId
     * @param userTagsEnum
     * @return
     */
    boolean cancelTag(Long userId,UserTagsEnum userTagsEnum);

    /**
     * Check whether the user contains the tags;
     *
     * @param userId
     * @param userTagsEnum
     * @return
     */
    boolean containTag(Long userId,UserTagsEnum userTagsEnum);
}
