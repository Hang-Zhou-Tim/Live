package org.hang.live.im.server.router.provider.service.impl;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.RpcContext;
import org.hang.live.im.core.server.interfaces.constants.ImCoreServerConstants;
import org.hang.live.im.core.server.interfaces.rpc.IRouterHandlerRPC;
import org.hang.live.im.core.server.interfaces.dto.ImMsgBody;
import org.hang.live.im.server.router.provider.service.ImRouterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @Author hang
 * @Date: Created in 10:30 2024/8/13
 * @Description
 */
@Service
public class ImRouterServiceImpl implements ImRouterService {

    @DubboReference(timeout = 30000)
    private IRouterHandlerRPC routerHandlerRpc;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(ImRouterServiceImpl.class);


    @Override
    public boolean sendMsg(ImMsgBody imMsgBody) {
        //Get ip of server that the user connects.
        String bindAddress = stringRedisTemplate.opsForValue().get(ImCoreServerConstants.IM_BIND_IP_KEY + imMsgBody.getAppId() + ":" + imMsgBody.getUserId());
        if (StringUtils.isEmpty(bindAddress)) {
            return false;
        }
        bindAddress = bindAddress.substring(0,bindAddress.indexOf("%"));
        //Put it in the dubbo context (similar to ctx.addr() but not bind with channel).
        RpcContext.getContext().set("ip", bindAddress);
        routerHandlerRpc.sendMsg(imMsgBody);
        return true;
    }
    //I optimise the code for sending batch messages to users.
    //If there are 200 users connected to 10 server. We do not send all messages to the server in 200 times.
    //We can first group users that connects to same server ip together, and then send batch messages to each server for users who connects same ip.
    @Override
    public void batchSendMsg(List<ImMsgBody> imMsgBodyList) {
        //Get all users id.
        List<Long> userIdList = imMsgBodyList.stream().map(ImMsgBody::getUserId).collect(Collectors.toList());
        //Get map between user id and message.
        Map<Long, ImMsgBody> userIdMsgMap = imMsgBodyList.stream().collect(Collectors.toMap(ImMsgBody::getUserId, x -> x));
        //Get list of keys for ip address in Redis
        Integer appId = imMsgBodyList.get(0).getAppId();
        List<String> cacheKeyList = new ArrayList<>();
        userIdList.forEach(userId -> {
            String cacheKey = ImCoreServerConstants.IM_BIND_IP_KEY + appId + ":" + userId;
            cacheKeyList.add(cacheKey);
        });
        //Fetch ip of each users if the user's ip is not null.
        List<String> ipList = stringRedisTemplate.opsForValue().multiGet(cacheKeyList).stream().filter(x -> x != null).collect(Collectors.toList());
        Map<String, List<Long>> userIdMap = new HashMap<>();
        //Put users with same ip in same array.
        ipList.forEach(ip -> {
            String currentIp = ip.substring(0, ip.indexOf("%"));
            Long userId = Long.valueOf(ip.substring(ip.indexOf("%") + 1));
            List<Long> currentUserIdList = userIdMap.get(currentIp);
            if (currentUserIdList == null) {
                currentUserIdList = new ArrayList<>();
            }
            currentUserIdList.add(userId);
            userIdMap.put(currentIp, currentUserIdList);
        });

        //Send batch messages of receivers that connect same server ip to that server.
        for (String currentIp : userIdMap.keySet()) {
            RpcContext.getContext().set("ip", currentIp);
            List<ImMsgBody> batchSendMsgGroupByIpList = new ArrayList<>();
            List<Long> ipBindUserIdList = userIdMap.get(currentIp);
            for (Long userId : ipBindUserIdList) {
                ImMsgBody imMsgBody = userIdMsgMap.get(userId);
                batchSendMsgGroupByIpList.add(imMsgBody);
            }
            LOGGER.info("Sending notification message to websocket server ip: {}", currentIp);
            routerHandlerRpc.batchSendMsg(batchSendMsgGroupByIpList);
        }
    }
}
