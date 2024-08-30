package org.hang.live.user.payment.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.hang.live.user.payment.provider.dao.maper.IPayTopicMapper;
import org.hang.live.user.payment.provider.dao.po.PayTopicPO;
import org.hang.live.user.payment.provider.service.IPayTopicService;
import org.hang.live.common.interfaces.enums.CommonStatusEnum;
import org.springframework.stereotype.Service;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
@Service
public class PayTopicServiceImpl implements IPayTopicService {

    @Resource
    private IPayTopicMapper payTopicMapper;

    @Override
    public PayTopicPO getByCode(Integer code) {
        LambdaQueryWrapper<PayTopicPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PayTopicPO::getBizCode,code);
        queryWrapper.eq(PayTopicPO::getStatus, CommonStatusEnum.VALID_STATUS.getCode());
        queryWrapper.last("limit 1");
        return payTopicMapper.selectOne(queryWrapper);
    }
}
