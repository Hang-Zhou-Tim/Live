package org.hang.live.im.core.server.service.impl;

import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.hang.live.common.redis.configuration.key.ImCoreServerProviderCacheKeyBuilder;
import org.hang.live.common.interfaces.topic.ImCoreServerProviderTopicNames;
import org.hang.live.im.core.server.service.IMsgAckCheckService;
import org.hang.live.im.core.server.interfaces.dto.ImMsgBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @Author hang
 * @Date: Created in 20:46 2024/8/13
 * @Description
 */
@Service
public class MsgAckCheckServiceImpl implements IMsgAckCheckService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MsgAckCheckServiceImpl.class);

    @Resource
    private MQProducer mqProducer;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private ImCoreServerProviderCacheKeyBuilder cacheKeyBuilder;
    //When clients ack the message, delete this from Redis.
    @Override
    public void doMsgAck(ImMsgBody imMsgBody) {
        String key = cacheKeyBuilder.buildImAckMapKey(imMsgBody.getUserId(), imMsgBody.getAppId());
        redisTemplate.opsForHash().delete(key, imMsgBody.getMsgId());
        redisTemplate.expire(key,30, TimeUnit.MINUTES);
    }
    //When sending a message to a client, record this message in Redis,
    // so if ACK for this message is not arrival, the record will remain in Redis.
    //The remaining record represents that user does not get message or ACK is not sent. It will re-send the message.
    // Retry time here is 1, if it still failed, discard the message and remove record in Redis.
    @Override
    public void recordMsgAck(ImMsgBody imMsgBody, int times) {
        String key = cacheKeyBuilder.buildImAckMapKey(imMsgBody.getUserId(), imMsgBody.getAppId());
        redisTemplate.opsForHash().put(key, imMsgBody.getMsgId(), times);
        redisTemplate.expire(key,30, TimeUnit.MINUTES);
    }
    //Send delayed message in MQ, if this message is not received by user, resend it 5 second later.
    @Override
    public void sendDelayMsg(ImMsgBody imMsgBody) {
        String json = JSON.toJSONString(imMsgBody);
        Message message = new Message();
        message.setBody(json.getBytes());
        message.setTopic(ImCoreServerProviderTopicNames.LIVE_IM_ACK_MSG_TOPIC);
        //level 2: Delay 5s
        message.setDelayTimeLevel(2);
        try {
            SendResult sendResult = mqProducer.send(message);
            LOGGER.info("[MsgAckCheckServiceImpl] msg is {},sendResult is {}", json, sendResult);
        } catch (Exception e) {
            LOGGER.error("[MsgAckCheckServiceImpl] error is ", e);
        }
    }
    //Get the sending times for ack, it is already acked by client, return -1;
    @Override
    public int getMsgAckTimes(String msgId, long userId, int appId) {
        Object value = redisTemplate.opsForHash().get(cacheKeyBuilder.buildImAckMapKey(userId, appId), msgId);
        if (value == null) {
            return -1;
        }
        return (int) value;
    }
}
