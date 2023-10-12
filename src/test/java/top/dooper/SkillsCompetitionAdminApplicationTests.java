package top.dooper;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.dooper.sys.entity.User;
import top.dooper.sys.mapper.UserMapper;
import top.dooper.utils.PasswordUtils;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class SkillsCompetitionAdminApplicationTests {

    @Test
    void testMapper() {
        System.out.println(PasswordUtils.hashPassword("123456"));
    }

}
