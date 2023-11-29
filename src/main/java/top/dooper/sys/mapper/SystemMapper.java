package top.dooper.sys.mapper;

import org.apache.ibatis.annotations.Select;
import top.dooper.sys.entity.System;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author AbuLan
 * @since 2023-11-25
 */
public interface SystemMapper extends BaseMapper<System> {

    @Select("update sc_system set title = #{title}, start_time = #{startTime}, end_time = #{endTime} where id = 1")
    void updateSystem(String title, String startTime, String endTime);

    @Select("select file_path from sc_system where id = 1")
    String getFilePath();
}
