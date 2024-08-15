package org.hang.live.im.router.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.hang.live.im.dto.ImMsgBody;
import org.hang.live.im.router.interfaces.rpc.ImRouterRpc;
import org.hang.live.im.router.provider.service.ImRouterService;

import java.util.List;

/**
 * @Author hang
 * @Date: Created in 10:29 2024/8/13
 * @Description
 */
@DubboService
public class ImRouterRpcImpl implements ImRouterRpc {

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
