package org.hang.live.living.interfaces.rpc;


import org.hang.live.common.interfaces.dto.PageWrapper;
import org.hang.live.living.interfaces.dto.LivingPkRespDTO;
import org.hang.live.living.interfaces.dto.LivingRoomReqDTO;
import org.hang.live.living.interfaces.dto.LivingRoomRespDTO;

import java.util.List;

/**
 * @Author hang
 * @Date: Created in 21:20 2024/8/14
 * @Description
 */
public interface ILivingRoomRpc {


    /**
     * Query all users' id by room id
     *
     * @param livingRoomReqDTO
     * @return
     */
    List<Long> queryUserIdByRoomId(LivingRoomReqDTO livingRoomReqDTO);

    /**
     * Query all rooms id
     *
     * @param livingRoomReqDTO
     * @return
     */
    PageWrapper<LivingRoomRespDTO> list(LivingRoomReqDTO livingRoomReqDTO);

    /**
     * Check if a user starts live streaming.
     *
     * @param roomId
     * @return
     */
    LivingRoomRespDTO queryByRoomId(Integer roomId);

    /**
     * Starts live stream rooms.
     *
     * @param livingRoomReqDTO
     * @return
     */
    Integer startLivingRoom(LivingRoomReqDTO livingRoomReqDTO);

    /**
     * Close live stream rooms.
     *
     * @param livingRoomReqDTO
     * @return
     */
    boolean closeLiving(LivingRoomReqDTO livingRoomReqDTO);

    /**
     * Online PK
     *
     * @param livingRoomReqDTO
     * @return
     */
    LivingPkRespDTO onlinePk(LivingRoomReqDTO livingRoomReqDTO);

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
    boolean offlinePk(LivingRoomReqDTO livingRoomReqDTO);
}
