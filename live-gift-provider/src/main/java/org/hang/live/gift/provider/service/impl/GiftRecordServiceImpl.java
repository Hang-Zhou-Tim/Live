package org.hang.live.gift.provider.service.impl;

import jakarta.annotation.Resource;
import org.hang.live.common.interfaces.utils.ConvertBeanUtils;
import org.hang.live.gift.dto.GiftRecordDTO;
import org.hang.live.gift.provider.dao.mapper.GiftRecordMapper;
import org.hang.live.gift.provider.dao.po.GiftRecordPO;
import org.hang.live.gift.provider.service.IGiftRecordService;
import org.springframework.stereotype.Service;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
@Service
public class GiftRecordServiceImpl implements IGiftRecordService {

    @Resource
    private GiftRecordMapper giftRecordMapper;

    @Override
    public void insertOne(GiftRecordDTO giftRecordDTO) {
        GiftRecordPO giftRecordPO = ConvertBeanUtils.convert(giftRecordDTO,GiftRecordPO.class);
        giftRecordMapper.insert(giftRecordPO);
    }
}
