package org.hang.live.api.service.impl;

import org.apache.dubbo.config.annotation.DubboReference;
//import org.hang.live.api.error.ApiErrorEnum;
import org.hang.live.api.service.ILiveStreamRoomService;
import org.hang.live.api.vo.LiveStreamRoomInitVO;
import org.hang.live.api.vo.req.LiveStreamRoomReqVO;
//import org.hang.live.api.vo.req.OnlinePkReqVO;
import org.hang.live.api.vo.req.OnlinePkReqVO;
import org.hang.live.api.vo.resp.LiveStreamRoomPageRespVO;
import org.hang.live.api.vo.resp.LiveStreamRoomRespVO;
import org.hang.live.api.vo.resp.RedPacketReceiveVO;
import org.hang.live.common.interfaces.dto.PageWrapper;
import org.hang.live.common.interfaces.utils.ConvertBeanUtils;
import org.hang.live.common.web.configuration.error.BizBaseErrorEnum;
import org.hang.live.common.web.configuration.error.ErrorAssert;
import org.hang.live.common.web.configuration.error.ErrorException;
import org.hang.live.gift.dto.RedPacketConfigReqDTO;
import org.hang.live.gift.dto.RedPacketConfigRespDTO;
import org.hang.live.gift.dto.RedPacketReceiveDTO;
import org.hang.live.gift.interfaces.IRedPacketConfigRPC;
import org.hang.live.im.core.server.interfaces.constants.AppIdEnum;
import org.hang.live.stream.room.interfaces.dto.LivePkStreamRoomRespDTO;
import org.hang.live.stream.room.interfaces.dto.LiveStreamRoomReqDTO;
import org.hang.live.stream.room.interfaces.dto.LiveStreamRoomRespDTO;
import org.hang.live.stream.room.interfaces.rpc.ILiveStreamRoomRPC;
import org.hang.live.user.dto.UserDTO;
import org.hang.live.user.interfaces.IUserRPC;
import org.hang.live.common.web.configuration.context.RequestContext;
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
public class LiveStreamRoomServiceImpl implements ILiveStreamRoomService {

    @DubboReference
    private IUserRPC userRpc;
    @DubboReference
    private ILiveStreamRoomRPC liveStreamRoomRPC;

    @DubboReference
    private IRedPacketConfigRPC redPacketConfigRPC;

    @Override
    public LiveStreamRoomPageRespVO list(LiveStreamRoomReqVO livingRoomReqVO) {
        PageWrapper<LiveStreamRoomRespDTO>  resultPage = liveStreamRoomRPC.list(ConvertBeanUtils.convert(livingRoomReqVO,LiveStreamRoomReqDTO.class));
        LiveStreamRoomPageRespVO livingRoomPageRespVO = new LiveStreamRoomPageRespVO();
        livingRoomPageRespVO.setList(ConvertBeanUtils.convertList(resultPage.getList(), LiveStreamRoomRespVO.class));
        livingRoomPageRespVO.setHasNext(resultPage.isHasNext());
        return livingRoomPageRespVO;
    }

    @Override
    public Integer startLiveStreamRoom(Integer type) {
        Long userId = RequestContext.getUserId();
        UserDTO userDTO = userRpc.getByUserId(userId);
        LiveStreamRoomReqDTO liveStreamRoomReqDTO = new LiveStreamRoomReqDTO();
        liveStreamRoomReqDTO.setAnchorId(userId);
        liveStreamRoomReqDTO.setRoomName("Anchor-" + RequestContext.getUserId() + "'s Room");
        liveStreamRoomReqDTO.setCovertImg(userDTO.getAvatar());
        liveStreamRoomReqDTO.setType(type);
        return liveStreamRoomRPC.startLiveStreamRoom(liveStreamRoomReqDTO);
    }

    //Handle when user close the live stream.
    @Override
    public boolean closeLiveStreamRoom(Integer roomId) {
        LiveStreamRoomReqDTO liveStreamRoomReqDTO = new LiveStreamRoomReqDTO();
        liveStreamRoomReqDTO.setRoomId(roomId);
        liveStreamRoomReqDTO.setAnchorId(RequestContext.getUserId());
        return liveStreamRoomRPC.closeLiveStreamRoom(liveStreamRoomReqDTO);
    }
    //When entered the room, get the details of user and anchor like nickname, userid, avatar, room background, etc.
    @Override
    public LiveStreamRoomInitVO getAnchorConfig(Long userId, Integer roomId) {
        LiveStreamRoomRespDTO respDTO = liveStreamRoomRPC.queryByRoomId(roomId);
        //ErrorAssert.isNotNull(respDTO,ApiErrorEnum.LIVING_ROOM_END);
        Map<Long,UserDTO> userDTOMap = userRpc.batchQueryUserInfo(Arrays.asList(respDTO.getAnchorId(),userId).stream().distinct().collect(Collectors.toList()));
        UserDTO anchor = userDTOMap.get(respDTO.getAnchorId());
        UserDTO watcher = userDTOMap.get(userId);
        LiveStreamRoomInitVO respVO = new LiveStreamRoomInitVO();
        respVO.setAnchorNickName(anchor.getNickName());
        respVO.setWatcherNickName(watcher.getNickName());
        respVO.setUserId(userId);
        //set avatar for the anchor
        respVO.setAvatar(StringUtils.isEmpty(anchor.getAvatar())?"https://s1.ax1x.com/2022/12/18/zb6q6f.png":anchor.getAvatar());
        respVO.setWatcherAvatar(watcher.getAvatar());
        if (respDTO == null || respDTO.getAnchorId() == null || userId == null) {
            //if the room is not existed
            respVO.setRoomId(-1);
            return respVO;
        }
        respVO.setRoomId(respDTO.getId());
        respVO.setAnchorId(respDTO.getAnchorId());
        respVO.setAnchor(respDTO.getAnchorId().equals(userId));

        // Check if the anchor has right to start red packet rain
        if (respVO.isAnchor()) {
            RedPacketConfigRespDTO redPacketConfigRespDTO = redPacketConfigRPC.queryByAnchorId(userId);
            if (redPacketConfigRespDTO != null) {
                respVO.setRedPacketConfigCode(redPacketConfigRespDTO.getConfigCode());
            }
        }
        respVO.setDefaultBgImg("https://picst.sunbangyan.cn/2023/08/29/waxzj0.png");
        return respVO;
    }

    @Override
    public boolean joinOnlinePK(OnlinePkReqVO onlinePkReqVO) {
        LiveStreamRoomReqDTO reqDTO = new LiveStreamRoomReqDTO();
        reqDTO.setRoomId(onlinePkReqVO.getRoomId());
        reqDTO.setAppId(AppIdEnum.LIVE_BIZ.getCode());
        reqDTO.setPkObjId(RequestContext.getUserId());
        LivePkStreamRoomRespDTO tryOnlineStatus = liveStreamRoomRPC.joinOnlinePK(reqDTO);
        ErrorAssert.isTure(tryOnlineStatus.isOnlineStatus(), new ErrorException(-1, tryOnlineStatus.getMsg()));
        return true;
    }

    @Override
    public Long queryOnlinePkUserId(Integer roomId) {
        return liveStreamRoomRPC.queryOnlinePkUserId(roomId);
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
    public RedPacketReceiveVO getRedPacket(Long userId, String code) {
        RedPacketConfigReqDTO reqDTO = new RedPacketConfigReqDTO();
        reqDTO.setUserId(userId);
        reqDTO.setRedPacketConfigCode(code);
        RedPacketReceiveDTO receiveDTO = redPacketConfigRPC.receiveRedPacket(reqDTO);
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
