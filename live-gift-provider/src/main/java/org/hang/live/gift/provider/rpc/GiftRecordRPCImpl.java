package org.hang.live.gift.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.hang.live.gift.dto.GiftRecordDTO;
import org.hang.live.gift.interfaces.IGiftRecordRPC;
import org.hang.live.gift.provider.service.IGiftRecordService;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
@DubboService
public class GiftRecordRPCImpl implements IGiftRecordRPC {

    @Resource
    private IGiftRecordService giftRecordService;

    @Override
    public void insertOne(GiftRecordDTO giftRecordDTO) {
        giftRecordService.insertOne(giftRecordDTO);
    }
}
