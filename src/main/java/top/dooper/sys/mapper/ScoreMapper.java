package top.dooper.sys.mapper;

import org.apache.ibatis.annotations.Select;
import top.dooper.sys.entity.Score;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author AbuLan
 * @since 2023-11-25
 */
public interface ScoreMapper extends BaseMapper<Score> {
    @Select("SELECT count(*) AS score_num FROM sc_score")
    Long getScoreNum();
}
