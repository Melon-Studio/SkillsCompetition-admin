package top.dooper.sys.mapper;

import org.apache.ibatis.annotations.Select;
import top.dooper.common.vo.PublishScoreVo;
import top.dooper.common.vo.RankVo;
import top.dooper.common.vo.SpaceWork;
import top.dooper.sys.entity.Rank;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author AbuLan
 * @since 2023-11-27
 */
public interface RankMapper extends BaseMapper<Rank> {
    Integer insertRank(List<Rank> data);
    @Select("select r.score, r.ranking from sc_rank r left join " +
            "sc_work w on r.work_id = w.id where w.sid = #{sid}  order by r.ranking ASC")
    List<SpaceWork> getData(String sid);

    @Select("select COUNT(*) as id from sc_rank")
    int hasDatas();
    List<RankVo> getDataVo();

    @Select("truncate table sc_rank")
    void clearData();

    @Select("truncate table sc_score")
    void clearScoreData();

    List<PublishScoreVo> getEmail();
}
