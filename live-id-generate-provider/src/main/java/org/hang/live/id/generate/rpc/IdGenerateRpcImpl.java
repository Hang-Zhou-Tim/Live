package org.hang.live.id.generate.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.hang.live.id.generate.interfaces.IdGenerateRPC;
import org.hang.live.id.generate.service.IdGenerateService;

/**
 * @Author hang
 * @Description
 */
@DubboService
public class IdGenerateRpcImpl implements IdGenerateRPC {

    @Resource
    private IdGenerateService idGenerateService;

    @Override
    public Long getSeqId(Integer id) {
        return idGenerateService.getSeqId(id);
    }

    @Override
    public Long getUnSeqId(Integer id) {
        return idGenerateService.getUnSeqId(id);
    }
}
