package org.hang.live.im.provider.service;

/**
 * Services that check if user is online
 *
 * @Author hang
 * @Date: Created in 09:29 2024/8/13
 * @Description
 */
public interface ImOnlineService {

    /**
     * Services that check if user is online
     *
     * @param userId
     * @param appId
     * @return
     */
    boolean isOnline(long userId,int appId);
}
