package org.hang.live.api.service;

import org.hang.live.api.vo.LivingRoomInitVO;
import org.hang.live.api.vo.req.LivingRoomReqVO;
//import org.hang.live.api.vo.req.OnlinePkReqVO;
import org.hang.live.api.vo.resp.LivingRoomPageRespVO;

/**
 * @Author hang
 * @Date: Created in 21:15 2023/8/13
 * @Description
 */
public interface ILivingRoomService {

    /**
     * list all live streams
     *
     * @param livingRoomReqVO
     * @return
     */
    LivingRoomPageRespVO list(LivingRoomReqVO livingRoomReqVO);

    /**
     * Deal with live stream room connection.
     *
     * @param type
     */
    Integer startingLiving(Integer type);


    /**
     * Deal with user pk live stream room connection.
     *
     * @param onlinePkReqVO
     * @return
     */
//    boolean onlinePk(OnlinePkReqVO onlinePkReqVO);

    /**
     * close live stream room
     *
     * @param roomId
     * @return
     */
    boolean closeLiving(Integer roomId);

    /**
     * Get anchor's details
     *
     * @param userId
     * @param roomId
     * @return
     */
    LivingRoomInitVO anchorConfig(Long userId,Integer roomId);

}
