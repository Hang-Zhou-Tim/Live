package org.hang.live.stream.room.interfaces.rpc;


import org.hang.live.common.interfaces.dto.PageWrapper;
import org.hang.live.stream.room.interfaces.dto.LivePkStreamRoomRespDTO;
import org.hang.live.stream.room.interfaces.dto.LiveStreamRoomReqDTO;
import org.hang.live.stream.room.interfaces.dto.LiveStreamRoomRespDTO;

import java.util.List;

/**
 * @Author hang
 * @Date: Created in 21:20 2024/8/14
 * @Description
 */
public interface ILiveStreamRoomRpc {


    /**
     * Query all users' id by room id
     *
     * @param livingRoomReqDTO
     * @return
     */
    List<Long> queryUserIdByRoomId(LiveStreamRoomReqDTO livingRoomReqDTO);

    /**
     * Query all rooms id
     *
     * @param livingRoomReqDTO
     * @return
     */
    PageWrapper<LiveStreamRoomRespDTO> list(LiveStreamRoomReqDTO livingRoomReqDTO);

    /**
     * Check if a user starts live stream room.
     *
     * @param roomId
     * @return
     */
    LiveStreamRoomRespDTO queryByRoomId(Integer roomId);

    /**
     * Starts live stream rooms.
     *
     * @param livingRoomReqDTO
     * @return
     */
    Integer startLiveStreamRoom(LiveStreamRoomReqDTO livingRoomReqDTO);

    /**
     * Close live stream rooms.
     *
     * @param livingRoomReqDTO
     * @return
     */
    boolean closeLiveStreamRoom(LiveStreamRoomReqDTO livingRoomReqDTO);

    LivePkStreamRoomRespDTO joinOnlinePK(LiveStreamRoomReqDTO livingRoomReqDTO);

    /**
     * Check who is involved in pk
     *
     * @param roomId
     * @return
     */
    Long queryOnlinePkUserId(Integer roomId);

    /**
     * User is offlined in pk.
     *
     * @param livingRoomReqDTO
     * @return
     */
    boolean leaveOnlinePK(LiveStreamRoomReqDTO livingRoomReqDTO);

    LiveStreamRoomRespDTO queryByAnchorId(Long userId);
}
