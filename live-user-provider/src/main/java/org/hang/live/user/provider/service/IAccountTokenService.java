package org.hang.live.user.provider.service;

/**
 * @Author hang
 * @Date: Created in 08:40 2024/8/11
 * @Description
 */
public interface IAccountTokenService {

    /**
     * Create Login Token
     *
     * @param userId
     * @return
     */
    String createAndSaveLoginToken(Long userId);

    /**
     * Validate User Token
     *
     * @param tokenKey
     * @return
     */
    Long getUserIdByToken(String tokenKey);
}
