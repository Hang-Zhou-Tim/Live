package org.hang.live.gift.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.hang.live.common.interfaces.utils.ConvertBeanUtils;
import org.hang.live.gift.dto.RedPacketConfigReqDTO;
import org.hang.live.gift.dto.RedPacketConfigRespDTO;
import org.hang.live.gift.dto.SnatchRedPacketDTO;
import org.hang.live.gift.interfaces.IRedPacketConfigRPC;
import org.hang.live.gift.provider.dao.po.RedPacketConfigPO;
import org.hang.live.gift.provider.service.IRedPacketConfigService;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
@DubboService
public class RedPacketConfigRPCImpl implements IRedPacketConfigRPC {
    
    @Resource
    private IRedPacketConfigService redPacketConfigService;

    @Override
    public RedPacketConfigRespDTO queryByAnchorId(Long anchorId) {
        return ConvertBeanUtils.convert(redPacketConfigService.queryByAnchorId(anchorId), RedPacketConfigRespDTO.class);
    }

    @Override
    public boolean addOne(RedPacketConfigReqDTO redPacketConfigReqDTO) {
        return redPacketConfigService.addOne(ConvertBeanUtils.convert(redPacketConfigReqDTO, RedPacketConfigPO.class));
    }

    @Override
    public boolean prepareRedPacket(Long anchorId) {
        return redPacketConfigService.prepareRedPacket(anchorId);
    }

    @Override
    public SnatchRedPacketDTO snatchRedPacket(RedPacketConfigReqDTO redPacketConfigReqDTO) {
        return redPacketConfigService.snatchRedPacket(redPacketConfigReqDTO);
    }

    @Override
    public Boolean startRedPacket(RedPacketConfigReqDTO reqDTO) {
        return redPacketConfigService.startRedPacket(reqDTO);
    }
}