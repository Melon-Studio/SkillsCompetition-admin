package top.dooper.sys.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.dooper.common.vo.ApiResponse;
import top.dooper.common.vo.RankVo;
import top.dooper.common.vo.SpaceWork;
import top.dooper.sys.entity.Rank;
import top.dooper.sys.entity.User;
import top.dooper.sys.service.IRankService;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author AbuLan
 * @since 2023-11-27
 */
@RestController
@RequestMapping("/api/rank")
public class RankController {

    @Autowired
    private IRankService rankService;

    @GetMapping("/getRanks")
    public ApiResponse<List<Rank>> getRanks(@RequestParam("token") String token) {
        List<Rank> data = rankService.getRanks(token);
        if (data != null) {
            return ApiResponse.success(data);
        }
        return ApiResponse.error(401, "Illegal operation, insufficient permissions.");
    }

    @GetMapping("/workInfo")
    public ApiResponse<List<SpaceWork>> workInfo(@RequestParam("token") String token) {
        List<SpaceWork> data = rankService.workInfo(token);
        if (data != null) {
            return ApiResponse.success(data);
        }
        return ApiResponse.error(401, "Illegal operation, insufficient permissions.");
    }

    @GetMapping("/hasData")
    public ApiResponse<Map<String, Boolean>> hasData(@RequestParam("token") String token) {
        Map<String, Boolean> data = rankService.hasData(token);
        if (data != null) {
            return ApiResponse.success(data);
        }
        return ApiResponse.error(401, "Illegal operation, insufficient permissions.");
    }

    @GetMapping("/pages")
    public ApiResponse<?> getPages(@RequestParam("token") String token){
        List<RankVo> pageNum = rankService.pages(token);
        if (pageNum != null){
            return ApiResponse.success(pageNum);
        }
        return ApiResponse.error(401, "Illegal operation, insufficient permissions.");
    }

    // 导出Excel表
    @GetMapping("/exportData")
    public Object exportData(@RequestParam("token") String token) {
        ResponseEntity<byte[]> data = rankService.exportData(token);
        if (data != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "成绩导出.xlsx"); // 替换为你的文件名

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(data.getBody());
        }
        return ApiResponse.error(401, "Illegal operation, insufficient permissions.");
    }

    @GetMapping("/clearTable")
    public ApiResponse<Map<String, Boolean>> clearTable(@RequestParam("token") String token) {
        Map<String, Boolean> data = rankService.clearData(token);
        if (data != null) {
            return ApiResponse.success(data);
        }
        return ApiResponse.error(401, "Illegal operation, insufficient permissions.");
    }

    @GetMapping("/publicationScore")
    public ApiResponse<?> publicationScore(@RequestParam("token") String token) {
        Boolean data = rankService.publicationScore(token);
        if (data != null) {
            return ApiResponse.success(data);
        }
        return ApiResponse.error(401, "Illegal operation, insufficient permissions.");
    }
}
