package org.hang.live.api.controller;

import jakarta.annotation.Resource;
import org.hang.live.api.service.IGiftService;
import org.hang.live.api.service.ILiveStreamRoomService;
import org.hang.live.api.vo.req.GiftReqVO;
import org.hang.live.api.vo.req.LiveStreamRoomReqVO;
import org.hang.live.api.vo.resp.GiftConfigVO;
import org.hang.live.common.interfaces.vo.WebResponseVO;
import org.hang.live.common.web.configuration.config.RequestLimit;
import org.hang.live.common.web.configuration.context.RequestContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/25
 * @Description
 */

@RestController
@RequestMapping("/gift")
public class GiftController {
    
    @Resource
    private IGiftService giftService;

    
    @PostMapping("/listGift")
    public WebResponseVO listGift() {
        List<GiftConfigVO> giftConfigVOS = giftService.listGift();
        return WebResponseVO.success(giftConfigVOS);
    }
    
    @PostMapping("/send")
    public WebResponseVO send(GiftReqVO giftReqVO) {
        return WebResponseVO.success(giftService.send(giftReqVO));
    }

    @RequestLimit(limit = 1, second = 10, msg = "Preparing Red Packet. Please Wait!")
    @PostMapping("/prepareRedPacket")
    public WebResponseVO prepareRedPacket(LiveStreamRoomReqVO livingRoomReqVO) {
        return WebResponseVO.success(giftService.prepareRedPacket(RequestContext.getUserId(), livingRoomReqVO.getRoomId()));
    }

    @RequestLimit(limit = 1, second = 10, msg = "Red Packet Rain Has Acticated!")
    @PostMapping("/startRedPacket")
    public WebResponseVO startRedPacket(LiveStreamRoomReqVO livingRoomReqVO) {
        return WebResponseVO.success(giftService.startRedPacket(RequestContext.getUserId(), livingRoomReqVO.getRedPacketConfigCode()));
    }

    @RequestLimit(limit = 1, second = 1, msg = "")
    @PostMapping("/snatchRedPacket")
    public WebResponseVO getRedPacket(LiveStreamRoomReqVO livingRoomReqVO) {
        return WebResponseVO.success(giftService.snatchRedPacket(RequestContext.getUserId(), livingRoomReqVO.getRedPacketConfigCode()));
    }
}