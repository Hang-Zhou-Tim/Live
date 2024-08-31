package org.hang.live.api.service;

import org.hang.live.api.vo.LiveStreamRoomInitVO;
import org.hang.live.api.vo.req.LiveStreamRoomReqVO;
import org.hang.live.api.vo.req.OnlinePkReqVO;
import org.hang.live.api.vo.resp.LiveStreamRoomPageRespVO;
import org.hang.live.api.vo.resp.RedPacketReceiveVO;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/21
 * @Description
 */
public interface ILiveStreamRoomService {

    /**
     * list all live streams
     *
     * @param livingRoomReqVO
     * @return
     */
    LiveStreamRoomPageRespVO list(LiveStreamRoomReqVO livingRoomReqVO);

    /**
     * Deal with live stream room connection.
     *
     * @param type
     */
    Integer startLiveStreamRoom(Integer type);


    /**
     * Deal with user pk live stream room connection.
     *
     * @param onlinePkReqVO
     * @return
     */
    boolean joinOnlinePK(OnlinePkReqVO onlinePkReqVO);

    /**
     * close live stream room
     *
     * @param roomId
     * @return
     */
    boolean closeLiveStreamRoom(Integer roomId);

    /**
     * Get anchor's details
     *
     * @param userId
     * @param roomId
     * @return
     */
    LiveStreamRoomInitVO getAnchorConfig(Long userId,Integer roomId);

    Long queryOnlinePkUserId(Integer roomId);

}
