package org.hang.live.user.provider.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.hang.live.user.provider.dao.po.UserPO;
/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/11
 * @Description
 */
@Mapper
public interface IUserMapper extends BaseMapper<UserPO> {

}
