package org.hang.live.gift.interfaces;

import org.hang.live.gift.dto.RedPacketConfigReqDTO;
import org.hang.live.gift.dto.RedPacketConfigRespDTO;
import org.hang.live.gift.dto.SnatchRedPacketDTO;

/**
 * CURD Red Packet Config RPC
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description Split a list into sublists of specific number.
 */
public interface IRedPacketConfigRPC {

    /**
     * check if the anchor has right to start red packet rain.
     */
    RedPacketConfigRespDTO queryByAnchorId(Long anchorId);
    
    /**
     * insert new red packet config.
     */
    boolean addOne(RedPacketConfigReqDTO redPacketConfigReqDTO);

    /**
     * prepare red packet rain.
     */
    boolean prepareRedPacket(Long anchorId);

    /**
     * users in the room can snatch red packet
     */
    SnatchRedPacketDTO snatchRedPacket(RedPacketConfigReqDTO redPacketConfigReqDTO);

    /**
     * start red packet rain
     */
    Boolean startRedPacket(RedPacketConfigReqDTO reqDTO);
    
}