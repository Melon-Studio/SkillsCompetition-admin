package top.dooper;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("top.dooper.sys.mapper")
public class SkillsCompetitionAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(SkillsCompetitionAdminApplication.class, args);
    }
}
