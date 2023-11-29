package top.dooper.sys.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import top.dooper.common.vo.HomeWork;
import top.dooper.sys.entity.Score;
import top.dooper.sys.entity.User;
import top.dooper.sys.mapper.ScoreMapper;
import top.dooper.sys.mapper.WorkMapper;
import top.dooper.sys.service.IScoreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author AbuLan
 * @since 2023-11-25
 */
@Service
public class ScoreServiceImpl extends ServiceImpl<ScoreMapper, Score> implements IScoreService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private WorkMapper workMapper;

    @Override
    public Integer rate(String token, Double score, String sid) {
        Object obj = redisTemplate.opsForValue().get(token);
        if (obj != null) {
            User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
            if (loginUser.getUsertype() < 1) {
                return null;
            }
            List<HomeWork> work = workMapper.getHomeWorkBySid(sid);
            int workId = work.get(0).getId();

            LambdaQueryWrapper<Score> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Score::getUserId, loginUser.getSid());
            wrapper.eq(Score::getWorkId, workId);
            Score data = this.baseMapper.selectOne(wrapper);
            if (data != null) {
                return -1;
            }


            String userId = loginUser.getSid();
            Score scoreEn = new Score();
            scoreEn.setUserId(userId);
            scoreEn.setWorkId(workId);
            scoreEn.setScore(score);
            int res = this.baseMapper.insert(scoreEn);
            return res;
        }
        return null;
    }

    @Override
    public List<Score> getAllData() {
        List<Score> data = new ArrayList<>();
        LambdaQueryWrapper<Score> wrapper = new LambdaQueryWrapper<>();
        data = this.baseMapper.selectList(wrapper);
        if (data != null) {
            return data;
        }
        return null;
    }


}
