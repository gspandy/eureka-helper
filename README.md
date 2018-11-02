# eureka工具
> 用于将一台eureka中的微服务提取下来，注册到另一个eureka中

## 配置
```
// 拉取微服务的注册中心地址
eureka.helper.grabURL=http\://10.206.20.197\:8761
// 即将注册微服务的注册中心地址
eureka.helper.registerURL=http\://localhost\:8888
// 要注册的微服务，多个以逗号隔开
eureka.helper.registerAppNames=ATTACHMENT-SERVICE,DATA-SERVICE
```
