package org.hang.live.living.provider.service;

import org.hang.live.living.interfaces.dto.LivingRoomReqDTO;

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
