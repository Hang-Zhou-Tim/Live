package org.hang.live.living.provider.service.impl;

import jakarta.annotation.Resource;
import org.hang.live.living.provider.service.ILivingRoomService;
import org.hang.live.framework.redis.starter.key.LivingProviderCacheKeyBuilder;
import org.hang.live.common.interfaces.enums.CommonStatusEum;
import org.hang.live.common.interfaces.utils.ConvertBeanUtils;
import org.hang.live.living.interfaces.dto.LivingRoomReqDTO;
import org.hang.live.living.interfaces.dto.LivingRoomRespDTO;
import org.hang.live.living.provider.dao.mapper.LivingRoomMapper;
import org.hang.live.living.provider.dao.mapper.LivingRoomRecordMapper;
import org.hang.live.living.provider.dao.po.LivingRoomRecordPO;
import org.hang.live.living.provider.service.ILivingRoomTxService;
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
    private LivingProviderCacheKeyBuilder cacheKeyBuilder;

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
        livingRoomRecordPO.setStatus(CommonStatusEum.INVALID_STATUS.getCode());
        livingRoomRecordMapper.insert(livingRoomRecordPO);
        livingRoomMapper.deleteById(livingRoomRecordPO.getId());
        //remove cache for the room.
        String cacheKey = cacheKeyBuilder.buildLivingRoomObj(livingRoomReqDTO.getRoomId());
        redisTemplate.delete(cacheKey);
        return true;
    }
}
