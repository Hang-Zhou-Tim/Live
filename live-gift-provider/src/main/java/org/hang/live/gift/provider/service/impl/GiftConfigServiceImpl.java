package org.hang.live.gift.provider.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.hang.live.common.redis.configuration.key.GiftProviderCacheKeyBuilder;
import org.hang.live.common.interfaces.enums.CommonStatusEnum;
import org.hang.live.common.interfaces.topic.GiftProviderTopicNames;
import org.hang.live.common.interfaces.utils.ConvertBeanUtils;
import org.hang.live.gift.dto.GiftConfigDTO;
import org.hang.live.gift.provider.dao.mapper.GiftConfigMapper;
import org.hang.live.gift.provider.dao.po.GiftConfigPO;
import org.hang.live.gift.provider.service.IGiftConfigService;
import org.hang.live.gift.provider.service.bo.GiftCacheRemoveBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
@Service
public class GiftConfigServiceImpl implements IGiftConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GiftConfigServiceImpl.class);

    @Resource
    private GiftConfigMapper giftConfigMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private GiftProviderCacheKeyBuilder cacheKeyBuilder;
    @Resource
    private MQProducer mqProducer;

    @Override
    public GiftConfigDTO getByGiftId(Integer giftId) {
        String cacheKey = cacheKeyBuilder.buildGiftConfigCacheKey(giftId);
        //Used redis cache to avoid slow IO performance in DB.
        GiftConfigDTO giftConfigDTO = (GiftConfigDTO) redisTemplate.opsForValue().get(cacheKey);
        if (giftConfigDTO != null) {
            //If it is not empty object then return it.
            if (giftConfigDTO.getGiftId() != null) {
                redisTemplate.expire(cacheKey, 60, TimeUnit.MINUTES);
                return giftConfigDTO;
            }
            //return null if the gift config is empty object.
            return null;
        }
        //If the Object is not in Redis, Query DB and Put Searched Gift Config into Redis.
        LambdaQueryWrapper<GiftConfigPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GiftConfigPO::getGiftId, giftId);
        queryWrapper.eq(GiftConfigPO::getStatus, CommonStatusEnum.VALID_STATUS.getCode());
        queryWrapper.last("limit 1");
        GiftConfigPO giftConfigPO = giftConfigMapper.selectOne(queryWrapper);
        if (giftConfigPO != null) {
            GiftConfigDTO configDTO = ConvertBeanUtils.convert(giftConfigPO, GiftConfigDTO.class);
            redisTemplate.opsForValue().set(cacheKey, configDTO, 60, TimeUnit.MINUTES);
            return configDTO;
        }
        // If there is no such gift, then cache the query with empty value to avoid void duplicate research.
        redisTemplate.opsForValue().set(cacheKey, new GiftConfigDTO(), 5, TimeUnit.MINUTES);
        return null;
    }

    @Override
    public List<GiftConfigDTO> queryGiftList() {
        String cacheKey = cacheKeyBuilder.buildGiftListCacheKey();
        //Since there is not much time we update gift config, it will be very suitable to cache all gift in Redis.
        List<GiftConfigDTO> cacheList = redisTemplate.opsForList().range(cacheKey, 0, 100).stream()
                .map(x -> (GiftConfigDTO) x).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(cacheList)) {
            //If it is not empty object then return it.
            if (cacheList.get(0).getGiftId() != null) {
                redisTemplate.expire(cacheKey, 60, TimeUnit.MINUTES);
                return cacheList;
            }
            return Collections.emptyList();
        }
        //If the List is Empty in Redis, Query DB and Put Searched Gift Config into Redis
        LambdaQueryWrapper<GiftConfigPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GiftConfigPO::getStatus, CommonStatusEnum.VALID_STATUS.getCode());
        List<GiftConfigPO> giftConfigPOList = giftConfigMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(giftConfigPOList)) {
            List<GiftConfigDTO> resultList = ConvertBeanUtils.convertList(giftConfigPOList, GiftConfigDTO.class);
            boolean trySetToRedis = redisTemplate.opsForValue().setIfAbsent(cacheKeyBuilder.buildGiftListLockCacheKey(),1,3,TimeUnit.SECONDS);
            if(trySetToRedis) {
                redisTemplate.opsForList().leftPushAll(cacheKey, resultList.toArray());
                //For most times, a live stream can take up to 1 hours.
                redisTemplate.expire(cacheKey, 60, TimeUnit.MINUTES);
            }
            return resultList;
        }
        //Store an empty list if there is no matched record.
        redisTemplate.opsForList().leftPush(cacheKey, new GiftConfigDTO());
        redisTemplate.expire(cacheKey, 5, TimeUnit.MINUTES);
        return Collections.emptyList();
    }
    //When a new gift is inserted, I remove the old one with double delayed deletion.
    @Override
    public void insertOne(GiftConfigDTO giftConfigDTO) {
        GiftConfigPO giftConfigPO = ConvertBeanUtils.convert(giftConfigDTO, GiftConfigPO.class);
        giftConfigPO.setStatus(CommonStatusEnum.VALID_STATUS.getCode());
        giftConfigMapper.insert(giftConfigPO);
        redisTemplate.delete(cacheKeyBuilder.buildGiftListCacheKey());
        GiftCacheRemoveBO giftCacheRemoveBO = new GiftCacheRemoveBO();
        giftCacheRemoveBO.setRemoveListCache(true);
        Message message = new Message();
        message.setTopic(GiftProviderTopicNames.REMOVE_GIFT_CACHE);
        message.setBody(JSON.toJSONBytes(giftCacheRemoveBO));
        //Consume after 1 second
        message.setDelayTimeLevel(1);
        try {
            SendResult sendResult = mqProducer.send(message);
            LOGGER.info("[insertOne] sendResult is {}", sendResult);
        } catch (Exception e) {
            LOGGER.info("[insertOne] mq send error: }", e);
        }
    }
    //When a gift is updated, I remove the old one with double delayed deletion.
    @Override
    public void updateOne(GiftConfigDTO giftConfigDTO) {
        GiftConfigPO giftConfigPO = ConvertBeanUtils.convert(giftConfigDTO, GiftConfigPO.class);
        giftConfigMapper.updateById(giftConfigPO);
        redisTemplate.delete(cacheKeyBuilder.buildGiftListCacheKey());
        redisTemplate.delete(cacheKeyBuilder.buildGiftConfigCacheKey(giftConfigDTO.getGiftId()));
        GiftCacheRemoveBO giftCacheRemoveBO = new GiftCacheRemoveBO();
        giftCacheRemoveBO.setRemoveListCache(true);
        giftCacheRemoveBO.setGiftId(giftConfigDTO.getGiftId());
        Message message = new Message();
        message.setTopic(GiftProviderTopicNames.REMOVE_GIFT_CACHE);
        message.setBody(JSON.toJSONBytes(giftCacheRemoveBO));
        message.setDelayTimeLevel(1);
        try {
            SendResult sendResult = mqProducer.send(message);
            LOGGER.info("[updateOne] sendResult is {}", sendResult);
        } catch (Exception e) {
            LOGGER.info("[updateOne] mq send error: }", e);
        }
    }


}
