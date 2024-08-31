package org.hang.live.im.core.server.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.hang.live.im.core.server.interfaces.rpc.ImOnlineRPC;
import org.hang.live.im.core.server.service.ImOnlineService;

/**
 * @Author hang
 * @Date: Created in 09:28 2024/8/12
 * @Description
 */
@DubboService
public class ImOnlineRPCImpl implements ImOnlineRPC {

    @Resource
    private ImOnlineService imOnlineService;

    @Override
    public boolean isOnline(long userId, int appId) {
        return imOnlineService.isOnline(userId,appId);
    }
}
