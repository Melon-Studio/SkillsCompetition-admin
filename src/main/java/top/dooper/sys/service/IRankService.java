package top.dooper.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.http.ResponseEntity;
import top.dooper.common.vo.RankVo;
import top.dooper.common.vo.SpaceWork;
import top.dooper.sys.entity.Rank;
import com.baomidou.mybatisplus.extension.service.IService;
import top.dooper.sys.entity.User;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author AbuLan
 * @since 2023-11-27
 */
public interface IRankService extends IService<Rank> {
    Integer addData(List<Rank> data);

    List<Rank> getRanks(String token);

    List<SpaceWork> workInfo(String token);

    Map<String, Boolean> hasData(String token);

    List<RankVo> pages(String token);

    ResponseEntity<byte[]> exportData(String token);

    Map<String, Boolean> clearData(String token);

    Boolean publicationScore(String token);
}
