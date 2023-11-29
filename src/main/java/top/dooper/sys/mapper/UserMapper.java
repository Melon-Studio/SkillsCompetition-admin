package top.dooper.sys.mapper;

import org.apache.ibatis.annotations.Select;
import top.dooper.sys.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author AbuLan
 * @since 2023-09-14
 */
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT COUNT(*) AS email_not_null_count " +
            "FROM sc_user " +
            "WHERE email IS NOT NULL;")
    Long getEmailNotNullCount();
}
