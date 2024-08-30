package org.hang.live.user.payment.provider.dao.maper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.hang.live.user.payment.provider.dao.po.CurrencyPO;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
@Mapper
public interface ICurrencyMapper extends BaseMapper<CurrencyPO> {
}
