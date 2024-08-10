package org.hang.live.id.generate.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.hang.live.id.generate.dao.po.IdGeneratePO;

import java.util.List;

/**
 * @Author Hang
 * @Description
 */
@Mapper
public interface IdGenerateMapper extends BaseMapper<IdGeneratePO> {
    //Here, instead of using xml files, we could simply use @Update to map with the methods below.
    //Getting the next id segment with the thinking of optimistic lock
    @Update("update t_id_generate_config set next_threshold=next_threshold+step," +
            "current_start=current_start+step,version=version+1 where id =#{id} and version=#{version}")
    int updateNewIdCountAndVersion(@Param("id")int id,@Param("version")int version);

    //Here, instead of using xml files, we could simply use @select to map with the methods below.
    @Select("select * from t_id_generate_config")
    List<IdGeneratePO> selectAll();
}
