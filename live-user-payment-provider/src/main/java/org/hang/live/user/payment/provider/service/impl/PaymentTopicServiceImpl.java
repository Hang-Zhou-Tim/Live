package org.hang.live.user.payment.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.hang.live.user.payment.provider.dao.maper.IPaymentTopicMapper;
import org.hang.live.user.payment.provider.dao.po.PaymentTopicPO;
import org.hang.live.common.interfaces.enums.CommonStatusEnum;
import org.hang.live.user.payment.provider.service.IPaymentTopicService;
import org.springframework.stereotype.Service;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
@Service
public class PaymentTopicServiceImpl implements IPaymentTopicService {

    @Resource
    private IPaymentTopicMapper paymentTopicMapper;

    @Override
    public PaymentTopicPO getByCode(Integer code) {
        LambdaQueryWrapper<PaymentTopicPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PaymentTopicPO::getBizCode,code);
        queryWrapper.eq(PaymentTopicPO::getStatus, CommonStatusEnum.VALID_STATUS.getCode());
        queryWrapper.last("limit 1");
        return paymentTopicMapper.selectOne(queryWrapper);
    }
}
