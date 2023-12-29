# SkillsCompetition-admin
此项目是 Skills Competition 的后端程序，使用 Java SpringBoot 开发，是为高校的网页设计比赛，提供的作品提交系统，欢迎计算机相关专业的同学克隆仓库学习代码。

### 项目搭建

1. 克隆仓库
```
git clone https://github.com/Melon-Studio/SkillsCompetition-admin.git
```
2. 新建 MySQL 数据库
3. 执行克隆到本地的 skillscompetition.sql 文件
4. 安装 Redis
5. 修改配置文件

`SkillsCompetition-admin -> src -> main -> resources -> application.yml`

```yaml
#服务器端口配置
server:
  port: 9999

#站点基础配置
WebApplication:
  #站点名称
  name: SkillsCompetition
  #站点地址
# url: http://localhost:5174/
  url: <前端项目地址>

#数据库配置
spring:
  datasource:
    username: <数据库账号>
    password: <数据库密码>
    url: jdbc:mysql://localhost:3306/<数据库名称>?useUnicode=true&characterEncoding=UTF-8
  #Json解析器配置
  jackson:
    serialization:
      FAIL_ON_EMPTY_BEANS: false
  #文件上传大小配置
  servlet:
    multipart:
      max-file-size: 20MB
      max-request-size: 20MB

  #Redis缓存池配置
  redis:
    port: 6379
    host: localhost

  #SMTP邮箱配置
  mail:
    #SMTP服务器地址
    host: <第三方邮箱SMTP地址>
    #SMTP服务器端口
    port: <第三方邮箱SMTP端口>
    #第三方邮箱用户名
    username: <第三方邮箱地址>
    #第三方邮箱授权码
    password: <第三方邮箱授权码>
    properties:
      mail:
        smtp:
          ssl:
            enable: true
          auth: true
    default-encoding: UTF-8

#日志配置
logging:
  level:
    top.dooper: debug

#异步跨域过滤
cors:
# filter: https://localhost:5174
  filter: <前端项目地址>

#MyBatis Plus 映射XML文件
mybatis-plus:
  mapper-locations: classpath*:/mapper/*Mapper.xml
```

6. 运行数据库和 Redis
7. 运行 Maven 重载下载缺失依赖
8. 运行项目

## 项目部署

1. 点击IDEA右侧Mevan图标

2. 展开Lifecycle，点击clean清理，再点击package打包

3. 将jar包运行到服务器

## 注意事项

在数据库中配置作品上传目录时，请使用绝对路径，配置到前端项目的 `public/work` 目录，这样前端才能正常访问到对应作品前端页面