package top.dooper.sys.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.val;
import org.omg.CORBA.TIMEOUT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import top.dooper.sys.entity.User;
import top.dooper.sys.mapper.UserMapper;
import top.dooper.sys.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.dooper.utils.JwtUtil;
import top.dooper.utils.PasswordUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author AbuLan
 * @since 2023-09-14
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Map<String, Object> login(User user) {
        user.setPassword(PasswordUtils.hashPassword(user.getPassword()));
        if (isValidUser(user)) {
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getSid, user.getSid());
            wrapper.eq(User::getPassword, user.getPassword());
            User loginUser = this.baseMapper.selectOne(wrapper);

            if (loginUser != null){
                String key = JwtUtil.generateToken(user.getUsername());

                // Redis
                loginUser.setPassword(null);
                redisTemplate.opsForValue().set(key, loginUser, 10, TimeUnit.DAYS);

                Map<String, Object> data = new HashMap<>();
                data.put("token", key);
                return data;
            }
            return null;
        } else {
            return null;
        }
    }

    @Override
    public Map<String, Object> getUserInfo(String token) {
        Object obj = redisTemplate.opsForValue().get(token);
        if (obj != null){
            User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
            Map<String, Object> data = new HashMap<>();
            data.put("name", loginUser.getUsername());
            data.put("sid", loginUser.getSid());
            data.put("isRoot", loginUser.getIsRoot());
            data.put("email", loginUser.getEmail());
            data.put("college", loginUser.getCollege());
            data.put("professional", loginUser.getProfessional());
            data.put("class", loginUser.getClass_());
            data.put("grade", loginUser.getGrade());
            data.put("status", loginUser.getStatus());

            return data;
        }
        return null;
    }

    @Override
    public void logout(String token) {
        redisTemplate.delete(token);
    }

    private boolean isValidUser(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getSid, user.getSid());
        wrapper.eq(User::getPassword, user.getPassword());

        User loginUser = this.baseMapper.selectOne(wrapper);

        if (loginUser != null){
            return true;
        }
        return false;
    }
}
