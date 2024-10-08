package org.hang.live.im.core.server.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.hang.live.im.core.server.interfaces.rpc.IRouterHandlerRPC;
import org.hang.live.im.core.server.service.IRouterHandlerService;
import org.hang.live.im.core.server.interfaces.dto.ImMsgBody;

import java.util.List;

/**
 * @Author hang
 * @Date: Created in 10:20 2024/8/13
 * @Description
 */
@DubboService(timeout = 30000)
public class RouterHandlerRPCImpl implements IRouterHandlerRPC {

    @Resource
    private IRouterHandlerService routerHandlerService;

    @Override
    public void sendMsg(ImMsgBody imMsgBody) {
        routerHandlerService.onReceive(imMsgBody);
    }

    @Override
    public void batchSendMsg(List<ImMsgBody> imMsgBodyList) {
        imMsgBodyList.forEach(imMsgBody -> {
            routerHandlerService.onReceive(imMsgBody);
        });
    }
}
