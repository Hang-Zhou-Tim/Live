package org.hang.live.account.interfaces;

/**
 * @Author hang
 * @Date: Created in 10:13 2024/8/10
 * @Description
 */
public interface IAccountTokenRPC {


    /**
     * Create a login token
     *
     * @param userId
     * @return
     */
    String createAndSaveLoginToken(Long userId);

    /**
     * Validate user's token
     *
     * @param tokenKey
     * @return
     */
    Long getUserIdByToken(String tokenKey);

}
