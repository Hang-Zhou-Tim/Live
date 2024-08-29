package org.hang.live.im.core.server.interfaces.rpc;

/**
 * RPC used to provide token for the user.
 *
 * @Author hang
 * @Date: Created in 09:28 2024/8/14
 * @Description
 */
public interface ImTokenRpc {

    /**
     * Create im token for user
     *
     * @param userId
     * @param appId
     * @return
     */
    String createImLoginToken(long userId, int appId);

    /**
     * get im token for user
     *
     * @param token
     * @return
     */
    Long getUserIdByToken(String token);
}
