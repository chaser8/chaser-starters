## spring-cloud-starter-gray
1.通过header或body中参数路由到指定服务的指定版本   
2.支持gateway和普通服务中的feign（开发中）   
3.目前只支持ribbon+nacos
### 1. maven配置
```xml
<dependency>
    <groupId>top.chaser.framework</groupId>
    <artifactId>spring-cloud-starter-gray</artifactId>
</dependency>
```
### 1. 应用配置
```yaml
chaser:
  gray:
    enable: true #是否启用默认true
    strategys: #配置策略
      - service-name: nacos-provider-a #服务名注意和nacos上的服务名对应上
        version: 1.1 #灰度版本
        percentage: 100 #流量比 0-100（即：0%-100%）
        conditions: #条件
          - type: param #条件取值类型，param or header
            #条件取值参数名 json格式时
	    #persion
	    #persion.name
	    #persons[3]
	    #person.friends[5].name	
            name: user.name 
            value: 123
          - type: header 
            name: regionId
            value: 028
      - service-name: nacos-provider-b
        version: 1.1
        percentage: 100
        conditions:
          - type: param
            name: user.name
            value: 123
```

