package org.hang.live.api.service.impl;

import org.apache.dubbo.config.annotation.DubboReference;
//import org.hang.live.api.error.ApiErrorEnum;
import org.hang.live.api.service.ILivingRoomService;
import org.hang.live.api.vo.LivingRoomInitVO;
import org.hang.live.api.vo.req.LivingRoomReqVO;
//import org.hang.live.api.vo.req.OnlinePkReqVO;
import org.hang.live.api.vo.resp.LivingRoomPageRespVO;
import org.hang.live.api.vo.resp.LivingRoomRespVO;
import org.hang.live.common.interfaces.dto.PageWrapper;
import org.hang.live.common.interfaces.utils.ConvertBeanUtils;
import org.hang.live.im.constants.AppIdEnum;
import org.hang.live.living.interfaces.dto.LivingPkRespDTO;
import org.hang.live.living.interfaces.dto.LivingRoomReqDTO;
import org.hang.live.living.interfaces.dto.LivingRoomRespDTO;
import org.hang.live.living.interfaces.rpc.ILivingRoomRpc;
import org.hang.live.user.dto.UserDTO;
import org.hang.live.user.interfaces.IUserRPC;
import org.hang.live.web.starter.context.RequestContext;
//import org.hang.live.web.starter.error.ErrorAssert;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author hang
 * @Date: Created in 21:15 2024/8/13
 * @Description
 */
@Service
public class LivingRoomServiceImpl implements ILivingRoomService {

    @DubboReference
    private IUserRPC userRpc;
    @DubboReference
    private ILivingRoomRpc livingRoomRpc;

    @Override
    public LivingRoomPageRespVO list(LivingRoomReqVO livingRoomReqVO) {
        PageWrapper<LivingRoomRespDTO>  resultPage = livingRoomRpc.list(ConvertBeanUtils.convert(livingRoomReqVO,LivingRoomReqDTO.class));
        LivingRoomPageRespVO livingRoomPageRespVO = new LivingRoomPageRespVO();
        livingRoomPageRespVO.setList(ConvertBeanUtils.convertList(resultPage.getList(), LivingRoomRespVO.class));
        livingRoomPageRespVO.setHasNext(resultPage.isHasNext());
        return livingRoomPageRespVO;
    }

    @Override
    public Integer startingLiving(Integer type) {
        Long userId = RequestContext.getUserId();
        UserDTO userDTO = userRpc.getByUserId(userId);
        LivingRoomReqDTO livingRoomReqDTO = new LivingRoomReqDTO();
        livingRoomReqDTO.setAnchorId(userId);
        livingRoomReqDTO.setRoomName("Anchor-" + RequestContext.getUserId() + "'s Room");
        livingRoomReqDTO.setCovertImg(userDTO.getAvatar());
        livingRoomReqDTO.setType(type);
        return livingRoomRpc.startLivingRoom(livingRoomReqDTO);
    }

//    @Override
//    public boolean onlinePk(OnlinePkReqVO onlinePkReqVO) {
//        LivingRoomReqDTO reqDTO = ConvertBeanUtils.convert(onlinePkReqVO,LivingRoomReqDTO.class);
//        reqDTO.setAppId(AppIdEnum.LIVE_BIZ.getCode());
//        reqDTO.setPkObjId(QiyuRequestContext.getUserId());
//        LivingPkRespDTO tryOnlineStatus = livingRoomRpc.onlinePk(reqDTO);
//        //ErrorAssert.isTure(tryOnlineStatus.isOnlineStatus(), new QiyuErrorException(-1,tryOnlineStatus.getMsg()));
//        return true;
//    }
    //Handle when user close the live stream.
    @Override
    public boolean closeLiving(Integer roomId) {
        LivingRoomReqDTO livingRoomReqDTO = new LivingRoomReqDTO();
        livingRoomReqDTO.setRoomId(roomId);
        livingRoomReqDTO.setAnchorId(RequestContext.getUserId());
        return livingRoomRpc.closeLiving(livingRoomReqDTO);
    }
    //When entered the room, get the details of user and anchor like nickname, userid, avatar, room background, etc.
    @Override
    public LivingRoomInitVO anchorConfig(Long userId, Integer roomId) {
        LivingRoomRespDTO respDTO = livingRoomRpc.queryByRoomId(roomId);
        //ErrorAssert.isNotNull(respDTO,ApiErrorEnum.LIVING_ROOM_END);
        Map<Long,UserDTO> userDTOMap = userRpc.batchQueryUserInfo(Arrays.asList(respDTO.getAnchorId(),userId).stream().distinct().collect(Collectors.toList()));
        UserDTO anchor = userDTOMap.get(respDTO.getAnchorId());
        UserDTO watcher = userDTOMap.get(userId);
        LivingRoomInitVO respVO = new LivingRoomInitVO();
        respVO.setAnchorNickName(anchor.getNickName());
        respVO.setWatcherNickName(watcher.getNickName());
        respVO.setUserId(userId);
        //set avatar for the anchor
        respVO.setAvatar(StringUtils.isEmpty(anchor.getAvatar())?"https://s1.ax1x.com/2022/12/18/zb6q6f.png":anchor.getAvatar());
        respVO.setWatcherAvatar(watcher.getAvatar());
        if (respDTO == null || respDTO.getAnchorId() == null || userId == null) {
            //if the room is not existed
            respVO.setRoomId(-1);
        } else {
            respVO.setRoomId(respDTO.getId());
            respVO.setAnchorId(respDTO.getAnchorId());
            respVO.setAnchor(respDTO.getAnchorId().equals(userId));
        }
        respVO.setDefaultBgImg("https://picst.sunbangyan.cn/2023/08/29/waxzj0.png");
        return respVO;
    }

}
