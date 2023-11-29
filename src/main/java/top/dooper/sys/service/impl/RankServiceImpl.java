package top.dooper.sys.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import top.dooper.common.vo.PublishScoreVo;
import top.dooper.common.vo.RankVo;
import top.dooper.common.vo.SpaceWork;
import top.dooper.service.IEmailService;
import top.dooper.sys.entity.Rank;
import top.dooper.sys.entity.User;
import top.dooper.sys.mapper.RankMapper;
import top.dooper.sys.service.IRankService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.dooper.utils.EmailUtil;
import top.dooper.utils.ExcelUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author AbuLan
 * @since 2023-11-27
 */
@Service
public class RankServiceImpl extends ServiceImpl<RankMapper, Rank> implements IRankService {
    @Autowired
    private RankMapper rankMapper;

    public RankServiceImpl(IEmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public Integer addData(List<Rank> data) {
        Integer i = rankMapper.insertRank(data);
        if (i != 0) {
            return 1;
        }
        return null;
    }

    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public List<Rank> getRanks(String token) {
        Object obj = redisTemplate.opsForValue().get(token);
        if (obj != null) {
            User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
            if (loginUser.getUsertype() < 2) {
                return null;
            }
            LambdaQueryWrapper<Rank> wrapper = new LambdaQueryWrapper<>();
            return this.baseMapper.selectList(wrapper);
        }
        return null;
    }

    @Override
    public List<SpaceWork> workInfo(String token) {
        Object obj = redisTemplate.opsForValue().get(token);
        if (obj != null) {
            User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
            return this.baseMapper.getData(loginUser.getSid());
        }
        return null;
    }

    @Override
    public Map<String, Boolean> hasData(String token) {
        Object obj = redisTemplate.opsForValue().get(token);
        if (obj != null) {
            User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
            if (loginUser.getUsertype() < 2) {
                return null;
            }
            int data = this.baseMapper.hasDatas();
            Map<String, Boolean> dataMap = new HashMap<>();

            if (data > 0) {
                dataMap.put("hasData", true);
                return dataMap;
            }
            dataMap.put("hasData", false);
            return dataMap;
        }
        return null;
    }

    @Override
    public List<RankVo> pages(String token) {
        Object obj = redisTemplate.opsForValue().get(token);
        if (obj != null){
            User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
            if(loginUser.getUsertype() < 2){
                return null;
            }
            List<RankVo> data = this.baseMapper.getDataVo();
            return data;
        }
        return null;
    }

    @Override
    public ResponseEntity<byte[]> exportData(String token) {
        Object obj = redisTemplate.opsForValue().get(token);
        if (obj != null) {
            User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
            if (loginUser.getUsertype() < 2) {
                return null;
            }
            List<RankVo> data = this.baseMapper.getDataVo();
            ResponseEntity<byte[]> excel = ExcelUtil.createExcelFile(data);
            return excel;
        }
        return null;
    }

    @Override
    public Map<String, Boolean> clearData(String token) {
        Object obj = redisTemplate.opsForValue().get(token);
        if (obj != null) {
            User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
            if (loginUser.getUsertype() < 2) {
                return null;
            }
            this.baseMapper.clearData();
            Map<String, Boolean> dataMap = new HashMap<>();
            dataMap.put("clearData", true);
            return dataMap;
        }
        return null;
    }

    private final IEmailService emailService;
    @Override
    public Boolean publicationScore(String token) {
        Object obj = redisTemplate.opsForValue().get(token);
        if (obj != null) {
            User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
            if (loginUser.getUsertype() < 2) {
                return null;
            }
            List<PublishScoreVo> data = this.baseMapper.getEmail();
            EmailUtil emailUtil = new EmailUtil(emailService);
            for (PublishScoreVo entity : data) {
                String email = entity.getEmail();
                String score = entity.getScore();
                String ranking = entity.getRanking();
                try {
                    emailUtil.sendMessage(email, score, ranking);
                }catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return true;
        }
        return null;
    }
}
