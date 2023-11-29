package top.dooper.sys.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.data.redis.core.RedisTemplate;
import top.dooper.sys.entity.System;
import top.dooper.sys.entity.User;
import top.dooper.sys.mapper.SystemMapper;
import top.dooper.sys.service.ISystemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author AbuLan
 * @since 2023-11-25
 */
@Service
public class SystemServiceImpl extends ServiceImpl<SystemMapper, System> implements ISystemService {

    private final RedisTemplate redisTemplate;

    public SystemServiceImpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Map<String, String> getTitle() {
        LambdaQueryWrapper<System> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(System::getId, 1);
        String title = this.baseMapper.selectOne(queryWrapper).getTitle();
        if (title != null) {
            Map<String, String> data = new HashMap<>();
            data.put("title", title);
            return data;
        }
        return null;
    }

    @Override
    public Map<String, String> getActivityTime() {
        LambdaQueryWrapper<System> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(System::getId, 1);
        List<System> data = this.baseMapper.selectList(queryWrapper);
        if (data != null) {
            Map<String, String> dataMap = new HashMap<>();
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            long start = Long.parseLong(data.get(0).getStartTime());
            long end = Long.parseLong(data.get(0).getEndTime());
            dataMap.put("startTime", sdf.format(new Date(start * 1000L)));
            dataMap.put("endTime", sdf.format(new Date(end * 1000L)));
            return dataMap;
        }
        return null;
    }

    @Override
    public List<System> getAll(String token) {
        Object obj = redisTemplate.opsForValue().get(token);
        if (obj != null) {
            User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
            if (loginUser.getUsertype() < 2) {
                return null;
            }
            LambdaQueryWrapper<System> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(System::getId, 1);
            return this.baseMapper.selectList(queryWrapper);
        }
        return null;
    }

    @Override
    public Integer setSetting(String token, System system) {
        Object obj = redisTemplate.opsForValue().get(token);
        if (obj != null) {
            User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
            if (loginUser.getUsertype() < 2) {
                return null;
            }
            java.lang.System.out.println(system);
            this.baseMapper.updateSystem(system.getTitle(), system.getStartTime(), system.getEndTime());
            return 1;
        }
        return null;
    }
}
