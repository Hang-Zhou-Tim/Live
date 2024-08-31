package org.hang.live.gift.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.hang.live.gift.dto.GiftConfigDTO;
import org.hang.live.gift.interfaces.IGiftConfigRPC;
import org.hang.live.gift.provider.service.IGiftConfigService;

import java.util.List;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
@DubboService
public class GiftConfigRPCImpl implements IGiftConfigRPC {

    @Resource
    private IGiftConfigService giftConfigService;

    @Override
    public GiftConfigDTO getByGiftId(Integer giftId) {
        return giftConfigService.getByGiftId(giftId);
    }

    @Override
    public List<GiftConfigDTO> queryGiftList() {
        return giftConfigService.queryGiftList();
    }

    @Override
    public void insertOne(GiftConfigDTO giftConfigDTO) {
        giftConfigService.insertOne(giftConfigDTO);
    }

    @Override
    public void updateOne(GiftConfigDTO giftConfigDTO) {
        giftConfigService.updateOne(giftConfigDTO);
    }
}
