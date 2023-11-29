package top.dooper.sys.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.dooper.common.vo.ApiResponse;
import top.dooper.sys.entity.User;
import top.dooper.sys.entity.Work;
import top.dooper.sys.service.IUserService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author AbuLan
 * @since 2023-09-14
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ApiResponse<Map<String, Object>> login(@RequestBody User user){
        Map<String, Object> data = userService.login(user);
        if (data != null){
            return ApiResponse.success(data);
        }
        return ApiResponse.error(400, "Student ID or Password error.");
    }

    @GetMapping("/info")
    public ApiResponse<Map<String, Object>> getUserInfo(@RequestParam("token") String token){
        Map<String, Object> userInfo = userService.getUserInfo(token);
        if (userInfo != null){
            return ApiResponse.success(userInfo);
        }
        return ApiResponse.error(401, "User logon authorization has expired.");
    }

    @PostMapping("/logout")
    public ApiResponse<?> logout(@RequestHeader("X-Token") String token) {
        userService.logout(token);
        return ApiResponse.success();
    }

    @DeleteMapping("/delete")
    public ApiResponse<?> delete(@RequestParam("sid") String sid, @RequestParam("token") String token){
        boolean status = userService.delete(sid, token);
        if (status){
            return ApiResponse.success();
        }
        return ApiResponse.error(401, "Illegal operation, insufficient permissions.");
    }

    @GetMapping("/pages")
    public ApiResponse<?> getPages(@RequestParam("token") String token, @RequestParam("page") Integer page, @RequestParam("keyword") String keyword){
        Page<User> pageNum = userService.pages(token, page, keyword);
        if (pageNum != null){
            return ApiResponse.success(pageNum);
        }
        return ApiResponse.error(401, "Illegal operation, insufficient permissions.");
    }

    @GetMapping("/sendVerifyCode")
    public ApiResponse<?> sendEmailCaptchaCode(@RequestParam("token") String token, @RequestParam("email") String email){
        Map<String, String> captchaCode = userService.sendEmailCaptchaCode(token, email);
        if (captchaCode != null){
            if(captchaCode.get("status") != null){
                return ApiResponse.error(406, "Other accounts are already bound to this email.");
            }
            return ApiResponse.success(captchaCode);
        }
        return ApiResponse.error(401, "Illegal operation, insufficient permissions.");
    }

    @GetMapping("/verifyEmail")
    public ApiResponse<?> verifyEmail(@RequestParam("token") String token, @RequestParam("key") String key, @RequestParam("captchaCode") String captchaCode, @RequestParam("password") String password, @RequestParam("email") String email){
        boolean status = userService.verifyEmail(token, key, captchaCode, password, email);
        if (status){
            return ApiResponse.success("Email verification success.");
        }
        return ApiResponse.error(405, "Email verification error.");
    }

    @GetMapping("dashboardData")
    public ApiResponse<List<Map<String, String>>> getDashboardData(@RequestParam("token") String token) {
        List<Map<String, String>> data = userService.getDashboardData(token);
        if (data != null) {
            return ApiResponse.success(data);
        }
        return ApiResponse.error(401, "Illegal operation, insufficient permissions.");
    }

    @PostMapping("/changePassword")
    public ApiResponse<?> changePassword(@RequestParam("token") String token, @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, @RequestParam("captchaCode") String captchaCode, @RequestParam("key") String key) {
        int data = userService.changePassword(token, oldPassword, newPassword, captchaCode, key);
        if (data == 1) {
            return ApiResponse.success();
        }
        if (data == -2) {
            return ApiResponse.error(425, "Key does not exist.");
        }
        if (data == 3) {
            return ApiResponse.error(426, "Verification code error.");
        }
        if (data == 0) {
            return ApiResponse.error(424, "Wrong password.");
        }
        return ApiResponse.error(401, "Illegal operation, insufficient permissions.");
    }

    @GetMapping("/sendVerifyCodes")
    public ApiResponse<?> sendEmailCaptchaCode(@RequestParam("token") String token){
        Map<String, String> captchaCode = userService.sendEmailCaptchaCode(token);
        if (captchaCode != null){
            return ApiResponse.success(captchaCode);
        }
        return ApiResponse.error(401, "Illegal operation, insufficient permissions.");
    }

}
