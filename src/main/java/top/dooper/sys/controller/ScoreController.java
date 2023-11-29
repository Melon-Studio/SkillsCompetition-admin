package top.dooper.sys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.dooper.common.vo.ApiResponse;
import top.dooper.sys.service.IScoreService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author AbuLan
 * @since 2023-11-25
 */
@RestController
@RequestMapping("/api/score")
public class ScoreController {
    @Autowired
    private final IScoreService scoreService;

    public ScoreController(IScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @PostMapping("/rate")
    public ApiResponse<?> rate(@RequestParam("token") String token, @RequestParam("score") Double score, @RequestParam("sid") String sid) {
        Integer data = scoreService.rate(token, score, sid);
        if (data != null) {
            if (data == -1) {
                return ApiResponse.error(421, "You have already rated this work. Repeat rating is prohibited.");
            }
            return ApiResponse.success();
        }
        return ApiResponse.error(401, "Illegal operation, insufficient permissions.");
    }
}
