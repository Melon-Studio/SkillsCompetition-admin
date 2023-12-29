package top.dooper.sys.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import top.dooper.service.IEmailService;
import top.dooper.sys.entity.User;
import top.dooper.sys.mapper.ScoreMapper;
import top.dooper.sys.mapper.UserMapper;
import top.dooper.sys.mapper.WorkMapper;
import top.dooper.sys.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.dooper.utils.EmailUtil;
import top.dooper.utils.JwtUtil;
import top.dooper.utils.PasswordUtils;

import java.text.DecimalFormat;
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

    private final RedisTemplate redisTemplate;

    private final IEmailService emailService;

    private final UserMapper userMapper;

    private final WorkMapper workMapper;

    private final ScoreMapper scoreMapper;

    public UserServiceImpl(RedisTemplate redisTemplate, IEmailService emailService, UserMapper userMapper, ScoreMapper scoreMapper, WorkMapper workMapper) {
        this.redisTemplate = redisTemplate;
        this.emailService = emailService;
        this.userMapper = userMapper;
        this.scoreMapper = scoreMapper;
        this.workMapper = workMapper;
    }

    @Override
    public Map<String, Object> login(User user) {
        boolean needInitUser = false;
        final String PASSWORD_PATTERN = "^[0-9]{6}$";
        if (user.getPassword().matches(PASSWORD_PATTERN)){
            needInitUser = true;
        }
        user.setPassword(PasswordUtils.hashPassword(user.getPassword()));
        if (isValidUser(user)) {
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getSid, user.getSid());
            wrapper.eq(User::getPassword, user.getPassword());
            User loginUser = this.baseMapper.selectOne(wrapper);

            if (loginUser != null){
                String key = JwtUtil.generateToken(user.getUsername());
                Map<String, Object> data = new HashMap<>();

                if(needInitUser){
                    data.put("needInitUser", true);
                }
                // Redis
                redisTemplate.opsForValue().set(key, loginUser, 10, TimeUnit.DAYS);


                data.put("token", key);
                return data;
            }
        }
        return null;
    }

    @Override
    public Map<String, Object> getUserInfo(String token) {
        Object obj = redisTemplate.opsForValue().get(token);
        if (obj != null){
            User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
            Map<String, Object> data = new HashMap<>();
            data.put("name", loginUser.getUsername());
            data.put("sid", loginUser.getSid());
            data.put("usertype", loginUser.getUsertype());
            data.put("email", loginUser.getEmail());
            data.put("college", loginUser.getCollege());
            data.put("password", loginUser.getPassword());
            data.put("professional", loginUser.getProfessional());
            data.put("classes", loginUser.getClasses());
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

    @Override
    public boolean delete(String sid, String token) {
        Object obj = redisTemplate.opsForValue().get(token);
        if (obj != null){
            User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
            if(loginUser.getUsertype() < 2){
                return false;
            }
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getSid, sid);
            baseMapper.delete(wrapper);
            return true;
        }
        return false;
    }

    @Override
    public Page<User> pages(String token, Integer page, String keyword) {
        Object obj = redisTemplate.opsForValue().get(token);
        if (obj != null){
            User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
            if(loginUser.getUsertype() < 2){
                return null;
            }
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.like(User::getSid, keyword);
            Page<User> userPage = this.baseMapper.selectPage(new Page<>(page, 10) ,wrapper);
            return userPage;
        }
        return null;
    }


    @Override
    public Map<String, String> sendEmailCaptchaCode(String token, String email) {
        Object obj = redisTemplate.opsForValue().get(token);
        if (obj != null){
            User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
            if(loginUser.getUsertype() < 0){
                return null;
            }
            String sid = loginUser.getSid();
            String time = String.valueOf(new Date().getTime());
            String key = DigestUtils.md5DigestAsHex((sid+time).getBytes());
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getEmail, email);
            List<User> emailList =  this.baseMapper.selectList(wrapper);
            if(!emailList.isEmpty()){
                Map<String, String> data = new HashMap<>();
                data.put("status", "1");
                return data;
            }
            try {
                EmailUtil emailUtil = new EmailUtil(emailService);
                String code = emailUtil.sendCaptchaMail(email);
                Map<String, String> data = new HashMap<>();
                data.put("sid", sid);
                data.put("time", time);
                data.put("code", code);
                redisTemplate.opsForValue().set(key, data, 5, TimeUnit.MINUTES);
                data.clear();
                data.put("key", key);
                return data;
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
        return null;
    }

    @Override
    public boolean verifyEmail(String token, String key, String captchaCode, String password, String email) {
        Object obj = redisTemplate.opsForValue().get(token);
        if (obj != null){
            User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
            if(loginUser.getUsertype() < 0){
                return false;
            }
            Object obj1 = redisTemplate.opsForValue().get(key);
            if (obj1 != null){
                Map<String, String> map = (Map<String, String>) obj1;
                if (map.get("code").equals(captchaCode)){
                    LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
                    wrapper.eq(User::getSid, loginUser.getSid());
                    wrapper.set(User::getPassword, PasswordUtils.hashPassword(password));
                    wrapper.set(User::getEmail, email);
                    this.update(wrapper);
                    redisTemplate.delete(key);
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    @Override
    public Long getInitUser(String token) {
        Object obj = redisTemplate.opsForValue().get(token);
        if (obj != null) {
            User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
            if (loginUser.getUsertype() < 2) {
                return -1L;
            }
            return userMapper.getEmailNotNullCount();
        }
        return null;
    }

    @Override
    public Long getNumberOfProjectsToGraded(String token) {
        Object obj = redisTemplate.opsForValue().get(token);
        if (obj != null) {
            User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
            if (loginUser.getUsertype() < 2) {
                return -1L;
            }
            // 获取作品数量
            Long workNum = workMapper.getWorkNumber();
            // 获取评分人员数量
            Long judgesNum = workMapper.getJudgesNum();
            // 获取评分表数量
            Long scoreNum = scoreMapper.getScoreNum();

            return workNum * judgesNum - scoreNum < 0 ? 0 : workNum * judgesNum - scoreNum;
        }
        return null;
    }

    @Override
    public String getScoringCompletion(String token) {
        Object obj = redisTemplate.opsForValue().get(token);
        if (obj != null) {
            User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
            if (loginUser.getUsertype() < 2) {
                return null;
            }
            // 获取作品数量
            Long workNum = workMapper.getWorkNumber(); //1
            // 获取评分人员数量
            Long judgesNum = workMapper.getJudgesNum(); //1
            // 获取评分表数量
            Long scoreNum = scoreMapper.getScoreNum(); //0
            System.out.println(workNum+"\n"+judgesNum+"\n"+scoreNum);
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            double data = (double) scoreNum / (workNum * judgesNum) * 100;
            System.out.println(data);
            if (Double.isNaN(data)) {
                return "暂无数据";
            }
            if (data == 0) {
                return 0+"%";
            }
            return decimalFormat.format(data) + "%";
        }
        return null;
    }

    @Override
    public List<Map<String, String>> getDashboardData(String token) {
        Object obj = redisTemplate.opsForValue().get(token);
        if (obj != null) {
            User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
            if (loginUser.getUsertype() < 2) {
                return null;
            }
            List<Map<String, String>> data = new ArrayList<>();
            Map<String, String> map = new HashMap<>();
            map.put("peopleNum", getInitUser(token).toString());
            map.put("workNum", workMapper.getWorkNumber().toString());
            map.put("proportion", getScoringCompletion(token));
            map.put("scoreNum", getNumberOfProjectsToGraded(token).toString());
            data.add(map);
            return data;
        }
        return null;
    }

    @Override
    public int changePassword(String token, String oldPassword, String newPassword, String captchaCode, String key) {
        Object obj = redisTemplate.opsForValue().get(token);
        if (obj != null) {
            User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
            if (loginUser.getUsertype() < 0) {
                return -1;
            }
            Object obj1 = redisTemplate.opsForValue().get(key);
            if (obj1 == null){
                return -2;
            }
            Map<String, String> map = (Map<String, String>) obj1;
            if (!map.get("code").equals(captchaCode)){
                return -3;
            }
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getSid, loginUser.getSid());
            wrapper.eq(User::getPassword, PasswordUtils.hashPassword(oldPassword));
            User news = new User();
            news.setPassword(PasswordUtils.hashPassword(newPassword));
            int update = this.baseMapper.update(news, wrapper);
            return update;
        }
        return -1;
    }

    @Override
    public Map<String, String> sendEmailCaptchaCode(String token) {
        Object obj = redisTemplate.opsForValue().get(token);
        if (obj != null){
            User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
            if(loginUser.getUsertype() < 0){
                return null;
            }
            String sid = loginUser.getSid();
            String time = String.valueOf(new Date().getTime());
            String key = DigestUtils.md5DigestAsHex((sid+time).getBytes());
            try {
                EmailUtil emailUtil = new EmailUtil(emailService);
                String code = emailUtil.sendCaptchaMail(loginUser.getEmail());
                Map<String, String> data = new HashMap<>();
                data.put("sid", sid);
                data.put("time", time);
                data.put("code", code);
                redisTemplate.opsForValue().set(key, data, 5, TimeUnit.MINUTES);
                data.clear();
                data.put("key", key);
                return data;
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
        return null;
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
