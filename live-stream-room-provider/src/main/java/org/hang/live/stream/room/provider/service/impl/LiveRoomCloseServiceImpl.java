package org.hang.live.stream.room.provider.service.impl;

import jakarta.annotation.Resource;
import org.hang.live.common.interfaces.enums.CommonStatusEnum;
import org.hang.live.stream.room.provider.service.ILiveStreamRoomService;
import org.hang.live.common.redis.configuration.key.StreamRoomProviderCacheKeyBuilder;
import org.hang.live.common.interfaces.utils.ConvertBeanUtils;
import org.hang.live.stream.room.interfaces.dto.LiveStreamRoomReqDTO;
import org.hang.live.stream.room.interfaces.dto.LiveStreamRoomRespDTO;
import org.hang.live.stream.room.provider.dao.mapper.LiveStreamRoomMapper;
import org.hang.live.stream.room.provider.dao.mapper.LiveStreamRoomRecordMapper;
import org.hang.live.stream.room.provider.dao.po.LiveStreamRoomRecordPO;
import org.hang.live.stream.room.provider.service.ILiveStreamRoomCloseService;
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
public class LiveRoomCloseServiceImpl implements ILiveStreamRoomCloseService {

    @Resource
    private ILiveStreamRoomService livingRoomService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private LiveStreamRoomRecordMapper LiveStreamRoomRecordMapper;
    @Resource
    private LiveStreamRoomMapper LiveStreamRoomMapper;
    @Resource
    private StreamRoomProviderCacheKeyBuilder cacheKeyBuilder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean closeLiveStreamRoom(LiveStreamRoomReqDTO LiveStreamRoomReqDTO) {
        LiveStreamRoomRespDTO LiveStreamRoomRespDTO = livingRoomService.queryByRoomId(LiveStreamRoomReqDTO.getRoomId());
        if (LiveStreamRoomRespDTO == null) {
            return false;
        }
        if (!(LiveStreamRoomRespDTO.getAnchorId().equals(LiveStreamRoomReqDTO.getAnchorId()))) {
            return false;
        }
        LiveStreamRoomRecordPO livingRoomRecordPO = ConvertBeanUtils.convert(LiveStreamRoomRespDTO, LiveStreamRoomRecordPO.class);
        livingRoomRecordPO.setEndTime(new Date());
        livingRoomRecordPO.setStatus(CommonStatusEnum.INVALID_STATUS.getCode());
        LiveStreamRoomRecordMapper.insert(livingRoomRecordPO);
        LiveStreamRoomMapper.deleteById(livingRoomRecordPO.getId());
        //remove cache for the room.
        String cacheKey = cacheKeyBuilder.buildLiveStreamRoomObj(LiveStreamRoomReqDTO.getRoomId());
        redisTemplate.delete(cacheKey);
        return true;
    }
}
