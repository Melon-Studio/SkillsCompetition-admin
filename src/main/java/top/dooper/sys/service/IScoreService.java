package top.dooper.sys.service;

import top.dooper.sys.entity.Score;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author AbuLan
 * @since 2023-11-25
 */
public interface IScoreService extends IService<Score> {
    Integer rate(String token, Double score, String sid);

    List<Score> getAllData();
}
