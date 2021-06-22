### 1. 说明
只适用于使用logback日志框架   

### 2. 引入依赖

在使用 top.chaser.framework:spring-boot-starter和top.chaser.framework:spring-boot-starter-web会默认引入，如未使用前面2个包请单独引入top.chaser.framework:chaser-starter-logging
```xml
<dependency>
    <groupId>top.chaser.framework</groupId>
    <artifactId>spring-boot-starter-logging</artifactId>
</dependency>
```

### 3. 应用配置
在工程配置文件或nacos中添加如下配置  
简单配置
```yaml
logging:
  config: classpath:logging-pub.xml #使用的配置文件logging-pub.xml（发布环境）或logging-dev.xml（发开环境），该配置文件在spring-boot-starter-logging包中
```
其他配置说明
```yaml
logging:
  level:
    root: info #日志级别默认info
  config: classpath:logging-pub.xml  #使用的配置文件logging-prod.xml（发布环境）或logging-dev.xml（发开环境），该配置文件在spring-boot-starter-logging包中
  path: ./logs #默认：./logs，日志保存路径
  file:
    max-history: 10 #默认值：10，保存多少天的日志文件，注意这里是根据日志文件生成日期来判断的，超过天数自动清除
    max-size: 10MB #默认值10MB，超过多大后分割压缩日志文件
    name: 1.log #默认：${spring.application.name}-${server.port}.log，日志文件名
```
==其他配置同springboot默认配置==

### 4. 运行效果

![](https://yangzb-res.oss-cn-beijing.aliyuncs.com/tydic/1592465280269.1592465280384.png)
错误信息单独分离到.error文件中
