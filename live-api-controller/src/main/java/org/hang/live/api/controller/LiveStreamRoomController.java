package org.hang.live.api.controller;

import jakarta.annotation.Resource;
import org.hang.live.api.service.ILiveStreamRoomService;
import org.hang.live.api.vo.LiveStreamRoomInitVO;
import org.hang.live.api.vo.req.LiveStreamRoomReqVO;
//import org.hang.live.api.vo.req.OnlinePkReqVO;
import org.hang.live.api.vo.req.OnlinePkReqVO;
import org.hang.live.common.interfaces.vo.WebResponseVO;
//import org.hang.live.web.starter.config.RequestLimit;
import org.hang.live.common.web.configuration.config.RequestLimit;
import org.hang.live.common.web.configuration.context.RequestContext;
//import org.hang.live.web.starter.error.BizBaseErrorEnum;
//import org.hang.live.web.starter.error.ErrorAssert;
import org.hang.live.common.web.configuration.error.BizBaseErrorEnum;
import org.hang.live.common.web.configuration.error.ErrorAssert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author hang
 * @Date: Created in 21:14 2024/8/12
 * @Description
 */
@RestController
@RequestMapping("/room")
public class LiveStreamRoomController {

    @Resource
    private ILiveStreamRoomService liveStreamRoomService;


    @PostMapping("/list")
    public WebResponseVO list(LiveStreamRoomReqVO livingRoomReqVO) {
        ErrorAssert.isTure(livingRoomReqVO != null && livingRoomReqVO.getType() != null, BizBaseErrorEnum.PARAM_ERROR);
        ErrorAssert.isTure(livingRoomReqVO.getPage() > 0 && livingRoomReqVO.getPageSize() <= 100, BizBaseErrorEnum.PARAM_ERROR);
        return WebResponseVO.success(liveStreamRoomService.list(livingRoomReqVO));
    }

    @RequestLimit(limit = 1, second = 10, msg = "Request too frequent. Try it later.")
    @PostMapping("/startLiveStreamRoom")
    public WebResponseVO startingLiveStreamRoom(Integer type) {
        ErrorAssert.isNotNull(type, BizBaseErrorEnum.PARAM_ERROR);
        Integer roomId = liveStreamRoomService.startLiveStreamRoom(type);
        LiveStreamRoomInitVO initVO = new LiveStreamRoomInitVO();
        initVO.setRoomId(roomId);
        return WebResponseVO.success(initVO);
    }

    @PostMapping("/joinOnlinePK")
    @RequestLimit(limit = 1,second = 3)
    public WebResponseVO joinOnlinePK(OnlinePkReqVO onlinePkReqVO) {
        ErrorAssert.isNotNull(onlinePkReqVO.getRoomId(), BizBaseErrorEnum.PARAM_ERROR);
        return WebResponseVO.success(liveStreamRoomService.joinOnlinePK(onlinePkReqVO));
    }

    @RequestLimit(limit = 1, second = 10, msg = "Request too frequent. Try it later.")
    @PostMapping("/closeLiveStreamRoom")
    public WebResponseVO closeLiving(Integer roomId) {
        ErrorAssert.isNotNull(roomId, BizBaseErrorEnum.PARAM_ERROR);
        boolean closeStatus = liveStreamRoomService.closeLiveStreamRoom(roomId);
        if (closeStatus) {
            return WebResponseVO.success();
        }
        return WebResponseVO.bizError("close live stream error");
    }

    /**
     * Get configuration of the anchor.
     *
     * @return
     */
    @PostMapping("/getAnchorConfig")
    public WebResponseVO getAnchorConfig(Integer roomId) {
        return WebResponseVO.success(liveStreamRoomService.getAnchorConfig(RequestContext.getUserId(), roomId));
    }

    @PostMapping("/queryOnlinePkUserId")
    public WebResponseVO queryOnlinePkUserId(Integer roomId) {
        return WebResponseVO.success(liveStreamRoomService.queryOnlinePkUserId(roomId));
    }

    @RequestLimit(limit = 1, second = 10, msg = "Preparing Red Packet. Please Wait!")
    @PostMapping("/prepareRedPacket")
    public WebResponseVO prepareRedPacket(LiveStreamRoomReqVO livingRoomReqVO) {
        return WebResponseVO.success(liveStreamRoomService.prepareRedPacket(RequestContext.getUserId(), livingRoomReqVO.getRoomId()));
    }

    @RequestLimit(limit = 1, second = 10, msg = "Red Packet Rain Has Acticated!")
    @PostMapping("/startRedPacket")
    public WebResponseVO startRedPacket(LiveStreamRoomReqVO livingRoomReqVO) {
        return WebResponseVO.success(liveStreamRoomService.startRedPacket(RequestContext.getUserId(), livingRoomReqVO.getRedPacketConfigCode()));
    }

    @RequestLimit(limit = 1, second = 1, msg = "")
    @PostMapping("/getRedPacket")
    public WebResponseVO getRedPacket(LiveStreamRoomReqVO livingRoomReqVO) {
        return WebResponseVO.success(liveStreamRoomService.getRedPacket(RequestContext.getUserId(), livingRoomReqVO.getRedPacketConfigCode()));
    }
}
