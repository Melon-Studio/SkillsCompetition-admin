package top.dooper;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class CodeGenerator {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:25565/skillscompetition?useUnicode=true&characterEncoding=UTF-8";
        String username = "SkillsCompetition";
        String password = "123456";
        String moduleName = "sys";
        String mapperLocation = "E:\\Project\\Java\\SpringBoot\\SkillsCompetition-admin\\src\\main\\resources\\mapper" + moduleName;
        String tablesName = "sc_rank";


        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author("AbuLan") // 设置作者
                            //.enableSwagger() // 开启 swagger 模式
                            //.fileOverride() // 覆盖已生成文件
                            .outputDir("E:\\Project\\Java\\SpringBoot\\SkillsCompetition-admin\\src\\main\\java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("top.dooper") // 设置父包名
                            .moduleName(moduleName) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, mapperLocation)); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude(tablesName) // 设置需要生成的表名
                            .addTablePrefix("sc_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
