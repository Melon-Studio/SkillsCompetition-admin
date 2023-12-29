package top.dooper.sys.mapper;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import top.dooper.common.vo.HomeWork;
import top.dooper.sys.entity.Work;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author AbuLan
 * @since 2023-11-20
 */
public interface WorkMapper extends BaseMapper<Work> {
    @Select("SELECT w.sid, w.path, w.time, w.name, u.username FROM sc_work w LEFT JOIN sc_user u ON w.sid = u.sid order by w.sid ASC")
    List<HomeWork> getHomeWork();

    @Select("SELECT w.id, w.sid, w.path, w.time, w.name, w.score, u.username FROM sc_work w LEFT JOIN sc_user u ON w.sid = u.sid WHERE #{sid} = w.sid order by w.id ASC")
    List<HomeWork> getHomeWorkBySid(String sid);

    @Select("SELECT count(*) AS work_number FROM sc_work")
    Long getWorkNumber();

    @Select("SELECT COUNT(*) AS total_reviewers " +
            "FROM sc_user " +
            "WHERE usertype = 1 or usertype = 2;")
    Long getJudgesNum();

    @Select("select path from sc_work where sid = #{sid}")
    String getPath(String sid);
}
