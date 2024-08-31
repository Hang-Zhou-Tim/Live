package org.hang.live.im.server.router.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.hang.live.im.core.server.interfaces.dto.ImMsgBody;
import org.hang.live.im.server.router.interfaces.rpc.ImRouterRPC;
import org.hang.live.im.server.router.provider.service.ImRouterService;

import java.util.List;

/**
 * @Author hang
 * @Date: Created in 10:29 2024/8/13
 * @Description
 */
@DubboService
public class ImRouterRPCImpl implements ImRouterRPC {

    @Resource
    private ImRouterService routerService;

    @Override
    public boolean sendMsg(ImMsgBody imMsgBody) {
        return routerService.sendMsg(imMsgBody);
    }

    // Send message in batch.
    @Override
    public void batchSendMsg(List<ImMsgBody> imMsgBodyList) {
        routerService.batchSendMsg(imMsgBodyList);
    }
}
