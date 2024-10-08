package org.hang.live.api.service.impl;

import com.alibaba.fastjson2.JSON;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.hang.live.api.error.ApiErrorEnum;
import org.hang.live.api.service.IGiftService;
import org.hang.live.api.vo.req.GiftReqVO;
import org.hang.live.api.vo.resp.GiftConfigVO;
import org.hang.live.api.vo.resp.RedPacketReceiveVO;
import org.hang.live.common.interfaces.dto.SendGiftMq;
import org.hang.live.common.interfaces.topic.GiftProviderTopicNames;
import org.hang.live.common.interfaces.utils.ConvertBeanUtils;
import org.hang.live.common.web.configuration.error.BizBaseErrorEnum;
import org.hang.live.gift.constants.SendGiftTypeEnum;
import org.hang.live.gift.dto.GiftConfigDTO;
import org.hang.live.gift.dto.RedPacketConfigReqDTO;
import org.hang.live.gift.dto.SnatchRedPacketDTO;
import org.hang.live.gift.dto.SnatchRedPacketDTO;
import org.hang.live.gift.interfaces.IGiftConfigRPC;
import org.hang.live.common.web.configuration.context.RequestContext;
import org.hang.live.common.web.configuration.error.ErrorAssert;
import org.hang.live.gift.interfaces.IRedPacketConfigRPC;
import org.hang.live.stream.room.interfaces.dto.LiveStreamRoomRespDTO;
import org.hang.live.stream.room.interfaces.rpc.ILiveStreamRoomRPC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/21
 * @Description
 */
@Service
public class GiftServiceImpl implements IGiftService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GiftServiceImpl.class);
    @DubboReference
    private IGiftConfigRPC giftConfigRPC;

    @DubboReference
    private IRedPacketConfigRPC redPacketConfigRPC;

    @DubboReference
    private ILiveStreamRoomRPC liveStreamRoomRPC;

    private Cache<Integer,GiftConfigDTO> giftConfigDTOCache = Caffeine.newBuilder().maximumSize(1000).expireAfterWrite(90, TimeUnit.SECONDS).build();

    @Resource
    private MQProducer mqProducer;

    @Override
    public List<GiftConfigVO> listGift() {
        List<GiftConfigDTO> giftConfigDTOList = giftConfigRPC.queryGiftList();
        return ConvertBeanUtils.convertList(giftConfigDTOList, GiftConfigVO.class);
    }

    @Override
    public boolean send(GiftReqVO giftReqVO) {
        int giftId = giftReqVO.getGiftId();
        //I used Caffine local cache map: if there exists id, return gift config, otherwise get it by RPC call.
        GiftConfigDTO giftConfigDTO = giftConfigDTOCache.get(giftId, id -> giftConfigRPC.getByGiftId(giftId));
        ErrorAssert.isNotNull(giftConfigDTO, ApiErrorEnum.GIFT_CONFIG_ERROR);
        ErrorAssert.isTure(!giftReqVO.getReceiverId().equals(giftReqVO.getSenderUserId()), ApiErrorEnum.NOT_SEND_TO_YOURSELF);
        SendGiftMq sendGiftMq = new SendGiftMq();
        sendGiftMq.setUserId(RequestContext.getUserId());
        sendGiftMq.setGiftId(giftId);
        sendGiftMq.setRoomId(giftReqVO.getRoomId());
        sendGiftMq.setReceiverId(giftReqVO.getReceiverId());
        sendGiftMq.setUrl(giftConfigDTO.getSvgaUrl());
        int sendGiftType = giftReqVO.getType();
        sendGiftMq.setType(sendGiftType);
        sendGiftMq.setPrice(giftConfigDTO.getPrice());
        //Used uuid to avoid duplicate consume
        sendGiftMq.setUuid(UUID.randomUUID().toString());
        Message message = new Message();
        if(SendGiftTypeEnum.PK_SEND_GIFT.getCode().equals(sendGiftType)){
            message.setTopic(GiftProviderTopicNames.SEND_PK_GIFT);
        }else{
            message.setTopic(GiftProviderTopicNames.SEND_GIFT);
        }

        message.setBody(JSON.toJSONBytes(sendGiftMq));
        try {
            SendResult sendResult = mqProducer.send(message);
            LOGGER.info("[gift-send] send result is {}", sendResult);
        } catch (Exception e) {
            LOGGER.info("[gift-send] send result is error:", e);
        }
        return true;
    }

    @Override
    public Boolean prepareRedPacket(Long userId, Integer roomId) {
        LiveStreamRoomRespDTO livingRoomRespDTO = liveStreamRoomRPC.queryByRoomId(roomId);
        ErrorAssert.isNotNull(livingRoomRespDTO, BizBaseErrorEnum.PARAM_ERROR);
        ErrorAssert.isTure(userId.equals(livingRoomRespDTO.getAnchorId()), BizBaseErrorEnum.PARAM_ERROR);
        return redPacketConfigRPC.prepareRedPacket(userId);
    }

    @Override
    public Boolean startRedPacket(Long userId, String code) {
        RedPacketConfigReqDTO reqDTO = new RedPacketConfigReqDTO();
        reqDTO.setUserId(userId);
        reqDTO.setRedPacketConfigCode(code);
        LiveStreamRoomRespDTO livingRoomRespDTO = liveStreamRoomRPC.queryByAnchorId(userId);
        ErrorAssert.isNotNull(livingRoomRespDTO, BizBaseErrorEnum.PARAM_ERROR);
        reqDTO.setRoomId(livingRoomRespDTO.getId());
        return redPacketConfigRPC.startRedPacket(reqDTO);
    }

    @Override
    public RedPacketReceiveVO snatchRedPacket(Long userId, String code) {
        RedPacketConfigReqDTO reqDTO = new RedPacketConfigReqDTO();
        reqDTO.setUserId(userId);
        reqDTO.setRedPacketConfigCode(code);
        
        SnatchRedPacketDTO receiveDTO = redPacketConfigRPC.snatchRedPacket(reqDTO);
        RedPacketReceiveVO respVO = new RedPacketReceiveVO();
        if (receiveDTO == null) {
            respVO.setMsg("Preparation of Red Packet Rain is finished!");
        } else {
            respVO.setPrice(receiveDTO.getPrice());
            respVO.setMsg(receiveDTO.getNotifyMsg());
        }
        return respVO;
    }
}