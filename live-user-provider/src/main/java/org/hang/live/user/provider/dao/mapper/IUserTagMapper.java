package org.hang.live.user.provider.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.hang.live.user.provider.dao.po.UserTagPO;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/11
 * @Description
 */
@Mapper
public interface IUserTagMapper extends BaseMapper<UserTagPO> {

    /**
     * Apply Or Operation to set the tag. This sql checks if tag is already contained for unnecessary update.
     *
     * @param userId
     * @param fieldName
     * @param tag
     * @return
     */
    @Update("update t_user_tag set ${fieldName}=${fieldName} | #{tag} where user_id=#{userId} and ${fieldName} & #{tag}=0")
    int setTag(Long userId, String fieldName, long tag);

    /**
     * Simulate the cancel tag operation: Reverse bits and then apply AND operation to the bit
     *
     * @param userId
     * @param fieldName
     * @param tag
     * @return
     */
    @Update("update t_user_tag set ${fieldName}=${fieldName} &~ #{tag} where user_id=#{userId} and ${fieldName} & #{tag}=#{tag}")
    int cancelTag(Long userId, String fieldName, long tag);
}
