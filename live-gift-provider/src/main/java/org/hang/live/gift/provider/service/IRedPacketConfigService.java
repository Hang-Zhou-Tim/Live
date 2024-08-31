package org.hang.live.gift.provider.service;

import org.hang.live.gift.dto.RedPacketConfigReqDTO;
import org.hang.live.gift.dto.SnatchRedPacketDTO;
import org.hang.live.gift.provider.dao.po.RedPacketConfigPO;
/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
public interface IRedPacketConfigService {

    /**
     * check whether the anchor has the right to start red packet rain.
     */
    RedPacketConfigPO queryByAnchorId(Long anchorId);

    /**
     * check red packet rain config by code. This is necessary because code cannot be guessed.
     */
    RedPacketConfigPO queryByConfigCode(String code);

    /**
     * insert a new check red packet rain config
     */
    boolean addOne(RedPacketConfigPO redPacketConfigPO);

    /**
     * update red packet rain settings.
     */
    boolean updateById(RedPacketConfigPO redPacketConfigPO);

    /**
     * prepare red packet list.
     */
    boolean prepareRedPacket(Long anchorId);

    /**
     * users snatch red packet.
     */
    SnatchRedPacketDTO snatchRedPacket(RedPacketConfigReqDTO redPacketConfigReqDTO);

    /**
     * start red packet rain.
     */
    Boolean startRedPacket(RedPacketConfigReqDTO reqDTO);

    /**
     * Async handle snatching red packet event after user has snatched a red packet.
     */
    void snatchedRedPacket(RedPacketConfigReqDTO reqDTO, Integer price);

}