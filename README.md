# SkillsCompetition-admin
此项目是 Skills Competition 的后端程序，使用 Java SpringBoot 开发，是为高校的网页设计比赛，提供的作品提交系统，欢迎计算机相关专业的同学克隆仓库学习代码。

### 项目搭建

1. 克隆仓库
```
git clone https://github.com/Melon-Studio/SkillsCompetition-admin.git
```
2. 新建 MySQL 数据库
3. 执行克隆到本地的 mysql.sql 文件
4. 安装 Redis
5. 修改配置文件

`SkillsCompetition-admin -> src -> main -> resources -> application.yml`

```yaml
#服务器配置
server:
  port: 9999 #端口配置

#数据库配置
spring:
  datasource:
    #数据库用户名
    username: SkillsCompetition
    #数据库密码
    password: 123456
    #拼接数据库连接字符串
    #jdbc:mysql://localhost:{数据库端口号}/{数据库名称}?useUnicode=true&characterEncoding=UTF-8
    url: jdbc:mysql://localhost:25565/skillscompetition?useUnicode=true&characterEncoding=UTF-8

  #Redis 配置
  redis:
    #Redis端口配置
    port: 6379
    host: localhost

#日志配置
logging:
  level:
    top.dooper: debug

#异步跨域过滤
cors:
  #配置前端域名，结尾不加‘/’
  filter: http://localhost:5173
```

6. 开启数据库和 Redis
7. 运行 Maven 重载下载缺失依赖