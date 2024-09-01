package org.hang.live.gift.provider.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.hang.live.gift.provider.dao.po.SkuStockInfoPO;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/30
 * @Description
 */
@Mapper
public interface ISkuStockInfoMapper extends BaseMapper<SkuStockInfoPO> {
    
    @Update("update t_sku_stock_info set stock_num = stock_num - #{num} where sku_id = #{skuId} and stock_num >= #{num}")
    boolean decrStockNumBySkuId(@Param("skuId") Long skuId, @Param("num") Integer num);
}