package org.hang.live.user.payment.provider.dao.maper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.hang.live.user.payment.provider.dao.po.AccountBalancePO;

/**
 * @Author hang
 * @Date: Created in 10:24 2024/8/20
 * @Description Manipulate Balance of User Account
 */
@Mapper
public interface IAccountBalanceMapper extends BaseMapper<AccountBalancePO> {

    @Update("update t_account_balance set current_balance = current_balance + #{num} where user_id = #{userId}")
    boolean incr(@Param("userId") long userId,@Param("num") int num);

    @Select("select current_balance from t_account_balance where user_id=#{userId} and status = 1 limit 1")
    Integer queryBalance(@Param("userId") long userId);

    @Update("update t_account_balance set current_balance = current_balance - #{num} where user_id = #{userId}")
    boolean decr(@Param("userId") long userId,@Param("num") int num);


}
