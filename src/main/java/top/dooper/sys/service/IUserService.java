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

    Map<String, String> sendEmailCaptchaCode(String token, String email);
    Map<String, String> sendEmailCaptchaCode(String token);
    boolean verifyEmail(String token, String key, String captchaCode, String password, String email);

    Long getInitUser(String token);

    Long getNumberOfProjectsToGraded(String token);

    String getScoringCompletion(String token);


    List<Map<String, String>> getDashboardData(String token);

    int changePassword(String token, String oldPassword, String newPassword, String captchaCode, String key);
}
