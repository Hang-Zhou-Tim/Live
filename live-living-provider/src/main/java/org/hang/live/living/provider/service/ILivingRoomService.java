package org.hang.live.living.provider.service;

import org.hang.live.common.interfaces.dto.PageWrapper;
import org.hang.live.im.core.server.interfaces.dto.ImOfflineDTO;
import org.hang.live.im.core.server.interfaces.dto.ImOnlineDTO;
import org.hang.live.living.interfaces.dto.LivingPkRespDTO;
import org.hang.live.living.interfaces.dto.LivingRoomReqDTO;
import org.hang.live.living.interfaces.dto.LivingRoomRespDTO;

import java.util.List;

/**
 * @Author hang
 * @Date: Created in 21:24 2024/8/10
 * @Description
 */
public interface ILivingRoomService {

    /**
     * Based on room Id to query all connected users‘ id.
     *
     * @param livingRoomReqDTO
     * @return
     */
    List<Long> queryUserIdByRoomId(LivingRoomReqDTO livingRoomReqDTO);

    /**
     * User offline handler.
     *
     * @param imOfflineDTO
     */
    void userOfflineHandler(ImOfflineDTO imOfflineDTO);

    /**
     * User online handler.
     *
     * @param imOnlineDTO
     */
    void userOnlineHandler(ImOnlineDTO imOnlineDTO);

     /**
     * List of live stream room type.
     *
     * @param type
     * @return
     */
    List<LivingRoomRespDTO> listAllLivingRoomFromDB(Integer type);

    /**
     * list pages of all users.
     *
     * @param livingRoomReqDTO
     * @return
     */
    PageWrapper<LivingRoomRespDTO> list(LivingRoomReqDTO livingRoomReqDTO);

    /**
     * based on roomId to query the room details/
     *
     * @param roomId
     * @return
     */
    LivingRoomRespDTO queryByRoomId(Integer roomId);

    /**
     * start live room
     *
     * @param livingRoomReqDTO
     * @return
     */
    Integer startLivingRoom(LivingRoomReqDTO livingRoomReqDTO);


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
    LivingPkRespDTO onlinePk(LivingRoomReqDTO livingRoomReqDTO);


    /**
     *  Deal with online pk disconnection request
     *
     * @param livingRoomReqDTO
     * @return
     */
    boolean offlinePk(LivingRoomReqDTO livingRoomReqDTO);
}
