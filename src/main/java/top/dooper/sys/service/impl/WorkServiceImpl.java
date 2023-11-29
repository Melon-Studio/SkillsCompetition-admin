package top.dooper.sys.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.dooper.common.vo.HomeWork;
import top.dooper.sys.entity.Rank;
import top.dooper.sys.entity.Score;
import top.dooper.sys.entity.User;
import top.dooper.sys.entity.Work;
import top.dooper.sys.mapper.SystemMapper;
import top.dooper.sys.mapper.WorkMapper;
import top.dooper.sys.service.IRankService;
import top.dooper.sys.service.IScoreService;
import top.dooper.sys.service.ISystemService;
import top.dooper.sys.service.IWorkService;
import top.dooper.utils.DirectoryUtils;
import top.dooper.utils.ScoreUtils;
import top.dooper.utils.UnPackageUtil;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author AbuLan
 * @since 2023-11-20
 */
@Service
public class WorkServiceImpl extends ServiceImpl<WorkMapper, Work> implements IWorkService {
    @Autowired
    private WorkMapper workMapper;

    @Autowired
    private ISystemService service;
    private final RedisTemplate redisTemplate;
    private String workDir;
    private final SystemMapper systemMapper;
    public WorkServiceImpl(RedisTemplate redisTemplate, SystemMapper systemMapper, IScoreService scoreService, IRankService rankService) {
        this.redisTemplate = redisTemplate;
        this.systemMapper = systemMapper;
        this.scoreService = scoreService;
        this.rankService = rankService;
        workDir = systemMapper.getFilePath();
    }



    @Override
    public Map<String, String> getPage(String sid) {
        LambdaQueryWrapper<Work> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Work::getSid, sid);
        Work work = this.baseMapper.selectOne(wrapper);
        if (work != null) {
            HashMap<String, String> map = new HashMap<>();
            map.put("path", work.getPath());
            return map;
        }
        return null;
    }

    @Override
    public List<HomeWork> getList() {
        List<HomeWork> data = workMapper.getHomeWork();
        if (data != null) {
            return data;
        }
        return null;
    }

    @Override
    public Integer uploadFile(MultipartFile file, String token) {
        Object obj = redisTemplate.opsForValue().get(token);
        if (obj != null) {
            User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
            try {
                Map<String, String> time = service.getActivityTime();
                long time1 = new Date().getTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date1 = simpleDateFormat.parse(time.get("startTime"));
                Date date2 = simpleDateFormat.parse(time.get("endTime"));
                if (time1 < date1.getTime() || time1 > date2.getTime()) {
                    return -4;
                }
                File tempFile = File.createTempFile("upload-", file.getOriginalFilename());
                file.transferTo(tempFile);

                int i = UnPackageUtil.unPackZip(tempFile, workDir, loginUser.getSid());
                switch (i) {
                    case 0:
                        addWorkInfo(loginUser.getSid());
                        return 0;
                    case -1: return -1;
                    case -2: return -2;
                    case -3: return -3;
                    case -4: return -4;
                    default: return -2;
                }
            }catch (Exception ex) {
                ex.printStackTrace();
                return -2;
            }
        }
        return null;
    }

    @Override
    public Integer addWorkInfo(String sid) {
        Work work = new Work();
        work.setSid(new Long(sid));
        work.setPath("./"+sid+"/index.html");
        work.setTime(String.valueOf(new Date().getTime()).substring(0, 10));
        int insert = this.baseMapper.insert(work);
        if (insert != 0) {
            return 1;
        }
        return null;
    }

    @Override
    public Integer addWorkInfo(String nameObj, String token) {
        Object obj = redisTemplate.opsForValue().get(token);
        if (obj != null) {
            User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
            JSONObject jsonObject = JSONObject.parseObject(nameObj);
            LambdaUpdateWrapper<Work> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Work::getSid, loginUser.getSid())
                    .set(Work::getName, jsonObject.get("name"));

            int updatedRows = this.baseMapper.update(null, updateWrapper);
            if (updatedRows != 0) {
                return 1;
            }
            return null;
        }
        return null;
    }

    @Override
    public Map<String, Boolean> hasWorkInfo(String token) {
        Object obj = redisTemplate.opsForValue().get(token);
        if (obj != null) {
            Map<String, Boolean> map = new HashMap<>();
            User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
            LambdaQueryWrapper<Work> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Work::getSid, loginUser.getSid());
            Work work = this.baseMapper.selectOne(queryWrapper);
            if (work != null) {
                map.put("hasWorkInfo", true);
                return map;
            }
            map.put("hasWorkInfo", false);
            return map;
        }
        return null;
    }

    @Override
    public Integer deleteWork(String token) {
        Object obj = redisTemplate.opsForValue().get(token);
        if (obj != null) {
            User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
            File folder = new File(workDir + "\\" + loginUser.getSid());
            DirectoryUtils.deleteFile(folder);
            LambdaQueryWrapper<Work> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Work::getSid, loginUser.getSid());
            int deletedRows = this.baseMapper.delete(queryWrapper);
            if (deletedRows != 0) {
                return 1;
            }
            return 0;
        }
        return null;
    }

    @Override
    public List<HomeWork> getWorkInfo(String token) {
        Object obj = redisTemplate.opsForValue().get(token);
        if (obj != null) {
            User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
            String sid = loginUser.getSid();
            List<HomeWork> work = this.workMapper.getHomeWorkBySid(sid);
            return work;
        }
        return null;
    }

    @Override
    public Page<Work> pages(String token, Integer page, String keyword) {
        Object obj = redisTemplate.opsForValue().get(token);
        if (obj != null){
            User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
            if(loginUser.getUsertype() < 2){
                return null;
            }
            LambdaQueryWrapper<Work> wrapper = new LambdaQueryWrapper<>();
            wrapper.like(Work::getSid, keyword);
            Page<Work> userPage = this.baseMapper.selectPage(new Page<>(page, 10) ,wrapper);
            return userPage;
        }
        return null;
    }

    private final IScoreService scoreService;
    private final IRankService rankService;
    @Override
    public Integer announced(String token) {
        List<Score> allData = scoreService.getAllData();
        List<Rank> averageScores = ScoreUtils.calculateAverageScore(allData);
        List<Rank> rankedScores = ScoreUtils.rankScores(averageScores);
        Integer i = rankService.addData(rankedScores);
        if (i != null) return 1;
        return null;
    }

    @Override
    public Map<String, String> getWorkCode(String token, String sid) {
        Object obj = redisTemplate.opsForValue().get(token);
        if (obj != null){
            User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
            if(loginUser.getUsertype() < 1){
                return null;
            }
            String path = this.baseMapper.getPath(sid);
            String str1 = path.substring(1, path.length());
            String outPath = workDir + str1;
            File html = new File(outPath);
            Map<String, String> keyValueMap = new HashMap<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(html))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                keyValueMap.put("html", content.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return keyValueMap;
        }
        return null;
    }

    @Override
    public Map<String, String> getFolderHierarchy(String token, String sid) {
        Object obj = redisTemplate.opsForValue().get(token);
        if (obj != null) {
            User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
            if (loginUser.getUsertype() < 1) {
                return null;
            }
            String path = this.baseMapper.getPath(sid);
            String str1 = path.substring(1, 12);
            String outPath = workDir + str1;
            Map<String, String> map = new HashMap<>();
            map.put("structure", DirectoryUtils.getDirectoryHierarchy(outPath));
            return map;
        }
        return null;
    }

}
