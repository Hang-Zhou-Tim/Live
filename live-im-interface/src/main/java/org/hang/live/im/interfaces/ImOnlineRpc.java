package org.hang.live.im.interfaces;

/**
 * RPC used to check if user is online
 *
 * @Author hang
 * @Date: Created in 09:28 2024/8/14
 * @Description
 */
public interface ImOnlineRpc {

    /**
     * Check if user is online
     *
     * @param userId
     * @param appId
     * @return
     */
    boolean isOnline(long userId,int appId);
}
