package top.dooper.sys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import top.dooper.common.vo.ApiResponse;
import top.dooper.sys.entity.System;
import top.dooper.sys.service.ISystemService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author AbuLan
 * @since 2023-11-25
 */
@RestController
@RequestMapping("/api/system")
public class SystemController {
    @Autowired
    ISystemService systemService;

    @GetMapping("/title")
    public ApiResponse<Map<String, String>> getTitle() {
        Map<String, String> data = systemService.getTitle();
        if (data != null) {
            return ApiResponse.success(data);
        }
        return ApiResponse.error(500, "Database don't have data.");
    }

    @GetMapping ("/activityTime")
    public ApiResponse<Map<String, String>> getActivityTime() {
        Map<String, String> data = systemService.getActivityTime();
        if (data != null) {
            return ApiResponse.success(data);
        }
        return ApiResponse.error(500, "Database don't have data.");
    }

    @GetMapping("/all")
    public ApiResponse<List<System>> getAll(@RequestParam("token") String token) {
        List<System> data = systemService.getAll(token);
        if (data != null) {
            return ApiResponse.success(data);
        }
        return ApiResponse.error(401, "Illegal operation, insufficient permissions.");
    }

    @PostMapping("/setSetting")
    public ApiResponse<System> setSetting(@RequestParam("token") String token, @RequestBody System system) {
        Integer data = systemService.setSetting(token, system);
        if (data != null) {
            return ApiResponse.success();
        }
        return ApiResponse.error(401, "Illegal operation, insufficient permissions.");
    }
}
