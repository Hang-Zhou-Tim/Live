package org.hang.live.im.provider.service;

/**
 * User login token service
 *
 * @Author hang
 * @Date: Created in 21:11 2024/8/12
 * @Description
 */

public interface ImTokenService {

    /**
     * Create im token
     *
     * @param userId
     * @param appId
     * @return
     */
    String createImLoginToken(long userId, int appId);

    /**
     * Based on IM token to query user id
     *
     * @param token
     * @return
     */
    Long getUserIdByToken(String token);
}
