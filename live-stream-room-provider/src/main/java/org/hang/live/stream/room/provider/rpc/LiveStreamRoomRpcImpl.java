package org.hang.live.stream.room.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.hang.live.stream.room.interfaces.dto.LivePkStreamRoomRespDTO;
import org.hang.live.stream.room.interfaces.dto.LiveStreamRoomReqDTO;
import org.hang.live.stream.room.interfaces.dto.LiveStreamRoomRespDTO;
import org.hang.live.stream.room.interfaces.rpc.ILiveStreamRoomRpc;
import org.hang.live.stream.room.provider.service.ILiveStreamRoomService;
import org.hang.live.stream.room.provider.service.ILiveStreamRoomCloseService;
import org.hang.live.common.interfaces.dto.PageWrapper;

import java.util.List;

/**
 * @Author hang
 * @Date: Created in 21:24 2024/8/13
 * @Description
 */
@DubboService
public class LiveStreamRoomRpcImpl implements ILiveStreamRoomRpc {

    @Resource
    private ILiveStreamRoomService liveStreamRoomService;
    @Resource
    private ILiveStreamRoomCloseService liveStreamRoomCloseService;

    @Override
    public List<Long> queryUserIdByRoomId(LiveStreamRoomReqDTO liveStreamRoomReqDTO) {
        return liveStreamRoomService.queryUserIdByRoomId(liveStreamRoomReqDTO);
    }

    @Override
    public PageWrapper<LiveStreamRoomRespDTO> list(LiveStreamRoomReqDTO liveStreamRoomReqDTO) {
        return liveStreamRoomService.list(liveStreamRoomReqDTO);
    }

    @Override
    public LiveStreamRoomRespDTO queryByRoomId(Integer roomId) {
        return liveStreamRoomService.queryByRoomId(roomId);
    }


    @Override
    public Integer startLiveStreamRoom(LiveStreamRoomReqDTO liveStreamRoomReqDTO) {
        return liveStreamRoomService.startLiveStreamRoom(liveStreamRoomReqDTO);
    }

    @Override
    public boolean closeLiveStreamRoom(LiveStreamRoomReqDTO liveStreamRoomReqDTO) {
        return liveStreamRoomCloseService.closeLiveStreamRoom(liveStreamRoomReqDTO);
    }

    @Override
    public LivePkStreamRoomRespDTO joinOnlinePK(LiveStreamRoomReqDTO liveStreamRoomReqDTO) {
        return liveStreamRoomService.joinOnlinePK(liveStreamRoomReqDTO);
    }

    @Override
    public Long queryOnlinePkUserId(Integer roomId) {
        return liveStreamRoomService.queryOnlinePkUserId(roomId);
    }

    @Override
    public boolean leaveOnlinePK(LiveStreamRoomReqDTO liveStreamRoomReqDTO) {
        return liveStreamRoomService.leaveOnlinePK(liveStreamRoomReqDTO);
    }

    @Override
    public LiveStreamRoomRespDTO queryByAnchorId(Long userId) {
       return liveStreamRoomService.queryByAnchorId(userId);
    }
}
