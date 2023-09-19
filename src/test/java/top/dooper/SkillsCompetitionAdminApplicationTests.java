package top.dooper;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.dooper.sys.entity.User;
import top.dooper.sys.mapper.UserMapper;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class SkillsCompetitionAdminApplicationTests {

    @Resource
    private UserMapper userMapper;
    @Test
    void testMapper() {
        List<User> users = userMapper.selectList(null);
        users.forEach(System.out::println);
    }

}
