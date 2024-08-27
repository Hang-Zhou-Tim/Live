package org.hang.live.id.generator.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.hang.live.id.generator.interfaces.IdGenerateRPC;
import org.hang.live.id.generator.service.IdGenerateService;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/11
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
