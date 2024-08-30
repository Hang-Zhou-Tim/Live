package org.hang.live.stream.room.provider.service;

import org.hang.live.stream.room.interfaces.dto.LiveStreamRoomReqDTO;

/**
 * @Author hang
 * @Date: Created in 19:21 2024/8/14
 * @Description
 */
public interface ILiveStreamRoomCloseService {

    /**
     * Close Live Stream
     *
     * @param livingRoomReqDTO
     * @return
     */
    boolean closeLiveStreamRoom(LiveStreamRoomReqDTO livingRoomReqDTO);

}
