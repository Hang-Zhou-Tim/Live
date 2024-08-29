package org.hang.live.stream.room.provider.service.impl;

import jakarta.annotation.Resource;
import org.hang.live.common.interfaces.enums.CommonStatusEnum;
import org.hang.live.stream.room.provider.service.ILivingRoomService;
import org.hang.live.common.redis.configuration.key.StreamRoomProviderCacheKeyBuilder;
import org.hang.live.common.interfaces.utils.ConvertBeanUtils;
import org.hang.live.stream.room.interfaces.dto.LivingRoomReqDTO;
import org.hang.live.stream.room.interfaces.dto.LivingRoomRespDTO;
import org.hang.live.stream.room.provider.dao.mapper.LivingRoomMapper;
import org.hang.live.stream.room.provider.dao.mapper.LivingRoomRecordMapper;
import org.hang.live.stream.room.provider.dao.po.LivingRoomRecordPO;
import org.hang.live.stream.room.provider.service.ILivingRoomTxService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Author hang
 * @Date: Created in 19:21 2024/8/12
 * @Description
 */
@Service
public class LivingRoomTxServiceImpl implements ILivingRoomTxService {

    @Resource
    private ILivingRoomService livingRoomService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private LivingRoomRecordMapper livingRoomRecordMapper;
    @Resource
    private LivingRoomMapper livingRoomMapper;
    @Resource
    private StreamRoomProviderCacheKeyBuilder cacheKeyBuilder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean closeLiving(LivingRoomReqDTO livingRoomReqDTO) {
        LivingRoomRespDTO livingRoomRespDTO = livingRoomService.queryByRoomId(livingRoomReqDTO.getRoomId());
        if (livingRoomRespDTO == null) {
            return false;
        }
        if (!(livingRoomRespDTO.getAnchorId().equals(livingRoomReqDTO.getAnchorId()))) {
            return false;
        }
        LivingRoomRecordPO livingRoomRecordPO = ConvertBeanUtils.convert(livingRoomRespDTO, LivingRoomRecordPO.class);
        livingRoomRecordPO.setEndTime(new Date());
        livingRoomRecordPO.setStatus(CommonStatusEnum.INVALID_STATUS.getCode());
        livingRoomRecordMapper.insert(livingRoomRecordPO);
        livingRoomMapper.deleteById(livingRoomRecordPO.getId());
        //remove cache for the room.
        String cacheKey = cacheKeyBuilder.buildLiveStreamRoomObj(livingRoomReqDTO.getRoomId());
        redisTemplate.delete(cacheKey);
        return true;
    }
}
