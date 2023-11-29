package top.dooper.sys.service;

import top.dooper.sys.entity.System;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author AbuLan
 * @since 2023-11-25
 */
public interface ISystemService extends IService<System> {

    Map<String, String> getTitle();

    Map<String, String> getActivityTime();

    List<System> getAll(String token);

    Integer setSetting(String token, System system);
}
