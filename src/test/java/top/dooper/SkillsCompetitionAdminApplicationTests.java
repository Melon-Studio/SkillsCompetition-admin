package top.dooper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import top.dooper.business.HtmlParser;
import top.dooper.service.IEmailService;
import top.dooper.sys.entity.Rank;
import top.dooper.sys.entity.Score;
import top.dooper.sys.service.IScoreService;
import top.dooper.sys.service.IUserService;
import top.dooper.sys.service.IWorkService;
import top.dooper.sys.service.impl.ScoreServiceImpl;
import top.dooper.sys.service.impl.WorkServiceImpl;
import top.dooper.utils.PasswordUtils;
import top.dooper.utils.ScoreUtils;

import javax.mail.MessagingException;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@SpringBootTest
class SkillsCompetitionAdminApplicationTests {

    @Test
    void testPassword() {
        System.out.println(PasswordUtils.hashPassword("123456"));
    }

//    @Test
//    void testCaptchaCodeGenerator() {
//        SMTPUtil smtpUtil = new SMTPUtil();
//        System.out.println(smtpUtil.codeGenerator());
//    }

    @Autowired
    private IEmailService emailService;
    @Autowired
    private IUserService userService;
    @Test
    void testSendEmail()  {
        userService.sendEmailCaptchaCode("eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2OTgyMTQ4MjJ9.ATK-SVQTkIAW2VMNg_kYxNgbOUxOtwtLgLNWlnL9RMotxu9luaOk-k32CmBDUJlNOgLgEqCCa9NmIV7GpwRBSQ","xiaofan6@foxmail.com");
    }

    @Test
    void testHtmlParser() throws URISyntaxException {
        File file = new File("E:/Project/HTML/小项目Demo/滚动元素浮现/index.html");
        HtmlParser htmlParser = new HtmlParser(file);
        Map<String, Object> stringObjectMap = htmlParser.specificationChecker();
        System.out.println(stringObjectMap);
    }

    @Test
    void getNum() {
        System.out.println(userService.getNumberOfProjectsToGraded("eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE3MDE5MTAxODV9.u_nHbYSL8zY0p5Tfqb52MLwmNHHDhnX066YAtvEcwkX2hN-9ubCRtGuHK2sHh5iK3WH_bSOQTutiEIGznppf_A"));
    }

    private IScoreService scoreService = new ScoreServiceImpl();
    @Test
    void scoreUtils() {
        List<Score> allData = scoreService.getAllData();
        List<Rank> averageScores = ScoreUtils.calculateAverageScore(allData);
        List<Rank> rankedScores = ScoreUtils.rankScores(averageScores);
        System.out.println(rankedScores);
    }
}
