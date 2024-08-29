package org.hang.live.stream.room.provider.service;

import org.hang.live.stream.room.interfaces.dto.LivingRoomReqDTO;

/**
 * @Author hang
 * @Date: Created in 19:21 2024/8/14
 * @Description
 */
public interface ILivingRoomTxService {

    /**
     * Close Live Stream
     *
     * @param livingRoomReqDTO
     * @return
     */
    boolean closeLiving(LivingRoomReqDTO livingRoomReqDTO);

}
