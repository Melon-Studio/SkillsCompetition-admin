package top.dooper.sys.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.dooper.common.vo.ApiResponse;
import top.dooper.common.vo.HomeWork;
import top.dooper.sys.entity.User;
import top.dooper.sys.entity.Work;
import top.dooper.sys.service.IWorkService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author AbuLan
 * @since 2023-11-20
 */
@RestController
@RequestMapping("/api/work")
public class WorkController {
    @Autowired
    private final IWorkService workService;

    public WorkController(IWorkService workService) { this.workService = workService; }

    @GetMapping("/{sid:\\d{10}}")
    public ApiResponse<Map<String, String>> getPage(@PathVariable("sid") String sid) {
        Map<String, String> page = workService.getPage(sid);
        if (page != null) {
            return ApiResponse.success(page);
        }
        return ApiResponse.error(404, "Not found data.");
    }

    @GetMapping("/all")
    public ApiResponse<List<HomeWork>> getAll() {
        List<HomeWork> data = workService.getList();
        if (data != null) {
            return ApiResponse.success(data);
        }
        return ApiResponse.error(404, "Not found data.");
    }

    @PostMapping(value =  "/uploadFile", headers = "Content-Type=multipart/form-data")
    public ApiResponse<?> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("token") String token) {
        Integer data = workService.uploadFile(file, token);
        if (data != null) {
            switch (data) {
                case 0: return ApiResponse.success("Upload success.");
                case -1: return ApiResponse.error(418, "The compressed package has a password. It cannot be operated.");
                case -2: return ApiResponse.error(419, "The compressed package could not be parsed.");
                case -3: return ApiResponse.error(420, "There is no index.html file in your archive root directory.");
                case -4: return ApiResponse.error(422, "Not within the time frame of submission.");
            }
        }
        return ApiResponse.error(401, "Illegal operation, insufficient permissions.");
    }

    @GetMapping("/pages")
    public ApiResponse<?> getPages(@RequestParam("token") String token, @RequestParam("page") Integer page, @RequestParam("keyword") String keyword){
        Page<Work> pageNum = workService.pages(token, page, keyword);
        if (pageNum != null){
            return ApiResponse.success(pageNum);
        }
        return ApiResponse.error(401, "Illegal operation, insufficient permissions.");
    }

    @PostMapping("/addWorkInfo")
    public ApiResponse<?> addWorkInfo(@RequestBody String name, @RequestParam("token") String token) {
        Integer data = workService.addWorkInfo(name, token);
        if (data != null){
            return ApiResponse.success("Add work name successfully");
        }
        return ApiResponse.error(401, "Illegal operation, insufficient permissions.");
    }

    @GetMapping("/hasWorkInfo")
    public ApiResponse<Map<String, Boolean>> hasWorkInfo(@RequestParam("token") String token) {
        Map<String, Boolean> data = workService.hasWorkInfo(token);
        if (data != null) {
            return ApiResponse.success(data);
        }
        return ApiResponse.error(401, "Illegal operation, insufficient permissions.");
    }

    @DeleteMapping("/deleteWork")
    public ApiResponse<?> deleteWork(@RequestParam("token") String token) {
        Integer data = workService.deleteWork(token);
        if (data != null) {
            if (data == 1) return ApiResponse.success();
            if (data == 0) return ApiResponse.error(423, "delete failed.");
        }
        return ApiResponse.error(401, "Illegal operation, insufficient permissions.");
    }

    @GetMapping("/getWorkInfo")
    public ApiResponse<List<HomeWork>> getWorkInfo(@RequestParam("token") String token) {
        List<HomeWork> data = workService.getWorkInfo(token);
        if (data != null) {
            return ApiResponse.success(data);
        }
        return ApiResponse.error(401, "Illegal operation, insufficient permissions.");
    }

    @GetMapping("/announced")
    public ApiResponse<?> announced(@RequestParam("token") String token) {
        Integer data = workService.announced(token);
        if (data != null) {
            return ApiResponse.success();
        }
        return ApiResponse.error(401, "Illegal operation, insufficient permissions.");
    }

    @GetMapping("/getWorkCode")
    public ApiResponse<Map<String, String>> getWorkCode(@RequestParam("token") String token, @RequestParam("sid") String sid) {
        Map<String, String> data = workService.getWorkCode(token, sid);
        if (data != null) {
            return ApiResponse.success(data);
        }
        return ApiResponse.error(401, "Illegal operation, insufficient permissions.");
    }

    @GetMapping("/getFolderHierarchy")
    public ApiResponse<Map<String, String>> getFolderHierarchy(@RequestParam("token") String token, @RequestParam("sid") String sid) {
        Map<String, String> data = workService.getFolderHierarchy(token, sid);
        if (data != null) {
            return ApiResponse.success(data);
        }
        return ApiResponse.error(401, "Illegal operation, insufficient permissions.");
    }

}
