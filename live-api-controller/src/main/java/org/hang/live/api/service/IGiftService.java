package org.hang.live.api.service;

import org.hang.live.api.vo.req.GiftReqVO;
import org.hang.live.api.vo.resp.GiftConfigVO;
import org.hang.live.api.vo.resp.RedPacketReceiveVO;

import java.util.List;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/21
 * @Description
 */
public interface IGiftService {
    //Get all gifts that the room contains.
    List<GiftConfigVO> listGift();
    //Send gifts in the room.
    boolean send(GiftReqVO giftReqVO);

    /**
     * Anchor clicks on prepare red packet rains
     */
    Boolean prepareRedPacket(Long userId, Integer roomId);

    /**
     * Anchor clicks on start red packet rains
     */
    Boolean startRedPacket(Long userId, String code);

    /**
     * users in the room get the red packet
     */
    RedPacketReceiveVO snatchRedPacket(Long userId, String redPacketConfigCode);
}