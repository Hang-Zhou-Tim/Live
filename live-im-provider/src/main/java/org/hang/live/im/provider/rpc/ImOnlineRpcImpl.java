package org.hang.live.im.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.hang.live.im.interfaces.ImOnlineRpc;
import org.hang.live.im.provider.service.ImOnlineService;

/**
 * @Author hang
 * @Date: Created in 09:28 2024/8/12
 * @Description
 */
@DubboService
public class ImOnlineRpcImpl implements ImOnlineRpc {

    @Resource
    private ImOnlineService imOnlineService;

    @Override
    public boolean isOnline(long userId, int appId) {
        return imOnlineService.isOnline(userId,appId);
    }
}