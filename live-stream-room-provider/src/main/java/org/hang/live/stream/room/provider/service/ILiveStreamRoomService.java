package org.hang.live.stream.room.provider.service;

import org.hang.live.common.interfaces.dto.PageWrapper;
import org.hang.live.im.core.server.interfaces.dto.ImOfflineDTO;
import org.hang.live.im.core.server.interfaces.dto.ImOnlineDTO;
import org.hang.live.stream.room.interfaces.dto.LivePkStreamRoomRespDTO;
import org.hang.live.stream.room.interfaces.dto.LiveStreamRoomReqDTO;
import org.hang.live.stream.room.interfaces.dto.LiveStreamRoomRespDTO;

import java.util.List;

/**
 * @Author hang
 * @Date: Created in 21:24 2024/8/10
 * @Description
 */
public interface ILiveStreamRoomService {

    /**
     * Based on room Id to query all connected users‘ id.
     *
     * @param liveStreamRoomReqDTO
     * @return
     */
    List<Long> queryUserIdByRoomId(LiveStreamRoomReqDTO liveStreamRoomReqDTO);

    /**
     * User offline handler.
     *
     * @param imOfflineDTO
     */
    void handleUserOfflineConnection(ImOfflineDTO imOfflineDTO);

    /**
     * User online handler.
     *
     * @param imOnlineDTO
     */
    void handleUserOnlineConnection(ImOnlineDTO imOnlineDTO);

     /**
     * List of live stream room type.
     *
     * @param type
     * @return
     */
    List<LiveStreamRoomRespDTO> listAllLiveStreamRoomsFromDB(Integer type);

    /**
     * list pages of all users.
     *
     * @param liveStreamRoomReqDTO
     * @return
     */
    PageWrapper<LiveStreamRoomRespDTO> list(LiveStreamRoomReqDTO liveStreamRoomReqDTO);

    /**
     * based on roomId to query the room details/
     *
     * @param roomId
     * @return
     */
    LiveStreamRoomRespDTO queryByRoomId(Integer roomId);

    /**
     * start live room
     *
     * @param liveStreamRoomReqDTO
     * @return
     */
    Integer startLiveStreamRoom(LiveStreamRoomReqDTO liveStreamRoomReqDTO);


    /**
     * query who is entered online pk
     *
     * @param roomId
     * @return
     */
    Long queryOnlinePkUserId(Integer roomId);

    /**
     * Deal with online pk connection request
     *
     * @param livingRoomReqDTO
     * @return
     */
    LivePkStreamRoomRespDTO joinOnlinePK(LiveStreamRoomReqDTO livingRoomReqDTO);


    /**
     *  Deal with online pk disconnection request
     *
     * @param liveStreamRoomReqDTO
     * @return
     */
    boolean leaveOnlinePK(LiveStreamRoomReqDTO liveStreamRoomReqDTO);

    LiveStreamRoomRespDTO queryByAnchorId(Long userId);
}
