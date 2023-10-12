package top.dooper.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.dooper.sys.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author AbuLan
 * @since 2023-09-14
 */
public interface IUserService extends IService<User> {

    Map<String, Object> login(User user);

    Map<String, Object> getUserInfo(String token);

    void logout(String token);

    boolean delete(String sid, String token);

    Page<User> pages(String token, Integer page, String keyword);
}
