package org.hang.live.im.core.server.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.hang.live.im.core.server.interfaces.rpc.ImTokenRpc;
import org.hang.live.im.core.server.service.ImTokenService;

/**
 * User token generation and retrieval.
 *
 * @Author hang
 * @Date: Created in 21:05 2024/8/12
 * @Description
 */
@DubboService
public class ImTokenRpcImpl implements ImTokenRpc {

    @Resource
    private ImTokenService imTokenService;

    @Override
    public String createImLoginToken(long userId, int appId) {
        return imTokenService.createImLoginToken(userId,appId);
    }

    @Override
    public Long getUserIdByToken(String token) {
        return imTokenService.getUserIdByToken(token);
    }
}
