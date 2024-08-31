package org.hang.live.api.service.impl;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.hang.live.api.service.ImService;
import org.hang.live.api.vo.resp.ImConfigVO;
import org.hang.live.im.core.server.interfaces.constants.AppIdEnum;
import org.hang.live.im.core.server.interfaces.rpc.ImTokenRPC;
import org.hang.live.common.web.configuration.context.RequestContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @Author hang
 * @Date: Created in 10:49 2028/8/13
 * @Description
 */
@Service
public class ImServiceImpl implements ImService {

    @DubboReference
    private ImTokenRPC imTokenRPC;
    @Value("${hang.im.ws.port}")
    private int port;
    @Resource
    private DiscoveryClient discoveryClient;

    @Override
    public ImConfigVO getImConfig() {
        ImConfigVO imConfigVO = new ImConfigVO();
        imConfigVO.setToken(imTokenRPC.createImLoginToken(RequestContext.getUserId(), AppIdEnum.LIVE_BIZ.getCode()));
        buildImServerAddress(imConfigVO);
        return imConfigVO;
    }

    private void buildImServerAddress(ImConfigVO imConfigVO) {
        List<ServiceInstance> serviceInstanceList = discoveryClient.getInstances("live-im-core-server");
        Collections.shuffle(serviceInstanceList);
        ServiceInstance aimInstance = serviceInstanceList.get(0);
        imConfigVO.setWsImServerAddress(aimInstance.getHost() + ":" + port);
    }
}
