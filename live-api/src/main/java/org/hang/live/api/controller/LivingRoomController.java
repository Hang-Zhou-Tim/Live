package org.hang.live.api.controller;

import jakarta.annotation.Resource;
import org.hang.live.api.service.ILivingRoomService;
import org.hang.live.api.vo.LivingRoomInitVO;
import org.hang.live.api.vo.req.LivingRoomReqVO;
//import org.hang.live.api.vo.req.OnlinePkReqVO;
import org.hang.live.common.interfaces.vo.WebResponseVO;
//import org.hang.live.web.starter.config.RequestLimit;
import org.hang.live.web.starter.context.RequestContext;
//import org.hang.live.web.starter.error.BizBaseErrorEnum;
//import org.hang.live.web.starter.error.ErrorAssert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author hang
 * @Date: Created in 21:14 2024/8/12
 * @Description
 */
@RestController
@RequestMapping("/live/api/living")
public class LivingRoomController {

    @Resource
    private ILivingRoomService livingRoomService;

    @PostMapping("/list")
    public WebResponseVO list(LivingRoomReqVO livingRoomReqVO) {
        //ErrorAssert.isTure(livingRoomReqVO != null && livingRoomReqVO.getType() != null, BizBaseErrorEnum.PARAM_ERROR);
        //ErrorAssert.isTure(livingRoomReqVO.getPage() > 0 && livingRoomReqVO.getPageSize() <= 100, BizBaseErrorEnum.PARAM_ERROR);
        return WebResponseVO.success(livingRoomService.list(livingRoomReqVO));
    }

//    @RequestLimit(limit = 1, second = 10, msg = "Request too frequent. Try it later.")
    @PostMapping("/startingLiving")
    public WebResponseVO startingLiving(Integer type) {
        //ErrorAssert.isNotNull(type, BizBaseErrorEnum.PARAM_ERROR);
        Integer roomId = livingRoomService.startingLiving(type);
        LivingRoomInitVO initVO = new LivingRoomInitVO();
        initVO.setRoomId(roomId);
        return WebResponseVO.success(initVO);
    }

//    @PostMapping("/onlinePk")
//    @RequestLimit(limit = 1,second = 3)
//    public WebResponseVO onlinePk(OnlinePkReqVO onlinePkReqVO) {
//        //ErrorAssert.isNotNull(onlinePkReqVO.getRoomId(), BizBaseErrorEnum.PARAM_ERROR);
//        return WebResponseVO.success(livingRoomService.onlinePk(onlinePkReqVO));
//    }

//    @RequestLimit(limit = 1, second = 10, msg = "Request too frequent. Try it later.")
    @PostMapping("/closeLiving")
    public WebResponseVO closeLiving(Integer roomId) {
        //ErrorAssert.isNotNull(roomId, BizBaseErrorEnum.PARAM_ERROR);
        boolean closeStatus = livingRoomService.closeLiving(roomId);
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
    @PostMapping("/anchorConfig")
    public WebResponseVO anchorConfig(Integer roomId) {
        return WebResponseVO.success(livingRoomService.anchorConfig(RequestContext.getUserId(), roomId));
    }

}
