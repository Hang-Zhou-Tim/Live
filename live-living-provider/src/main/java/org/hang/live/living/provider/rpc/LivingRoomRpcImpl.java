package org.hang.live.living.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.hang.live.living.provider.service.ILivingRoomService;
import org.hang.live.living.provider.service.ILivingRoomTxService;
import org.hang.live.common.interfaces.dto.PageWrapper;
import org.hang.live.living.interfaces.dto.LivingPkRespDTO;
import org.hang.live.living.interfaces.dto.LivingRoomReqDTO;
import org.hang.live.living.interfaces.dto.LivingRoomRespDTO;
import org.hang.live.living.interfaces.rpc.ILivingRoomRpc;

import java.util.List;

/**
 * @Author hang
 * @Date: Created in 21:24 2024/8/13
 * @Description
 */
@DubboService
public class LivingRoomRpcImpl implements ILivingRoomRpc {

    @Resource
    private ILivingRoomService livingRoomService;
    @Resource
    private ILivingRoomTxService livingRoomTxService;

    @Override
    public List<Long> queryUserIdByRoomId(LivingRoomReqDTO livingRoomReqDTO) {
        return livingRoomService.queryUserIdByRoomId(livingRoomReqDTO);
    }

    @Override
    public PageWrapper<LivingRoomRespDTO> list(LivingRoomReqDTO livingRoomReqDTO) {
        return livingRoomService.list(livingRoomReqDTO);
    }

    @Override
    public LivingRoomRespDTO queryByRoomId(Integer roomId) {
        return livingRoomService.queryByRoomId(roomId);
    }


    @Override
    public Integer startLivingRoom(LivingRoomReqDTO livingRoomReqDTO) {
        return livingRoomService.startLivingRoom(livingRoomReqDTO);
    }

    @Override
    public boolean closeLiving(LivingRoomReqDTO livingRoomReqDTO) {
        return livingRoomTxService.closeLiving(livingRoomReqDTO);
    }

    @Override
    public LivingPkRespDTO onlinePk(LivingRoomReqDTO livingRoomReqDTO) {
        return livingRoomService.onlinePk(livingRoomReqDTO);
    }

    @Override
    public Long queryOnlinePkUserId(Integer roomId) {
        return livingRoomService.queryOnlinePkUserId(roomId);
    }

    @Override
    public boolean offlinePk(LivingRoomReqDTO livingRoomReqDTO) {
        return livingRoomService.offlinePk(livingRoomReqDTO);
    }
}
