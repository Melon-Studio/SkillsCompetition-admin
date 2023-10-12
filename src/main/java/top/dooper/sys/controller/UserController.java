package top.dooper.sys.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.dooper.common.vo.ApiResponse;
import top.dooper.sys.entity.User;
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
    @Autowired
    private IUserService userService;

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

}
