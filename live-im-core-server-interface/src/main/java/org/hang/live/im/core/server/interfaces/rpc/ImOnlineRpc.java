package org.hang.live.im.core.server.interfaces.rpc;

/**
 * @Author hang
 * @Date: Created in 11:06 2024/8/13
 * @Description
 */
public interface ImOnlineRpc {
    boolean isOnline(long userId,int appId);
}