package org.example.springbootdemo.controller.microservice;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 微服务调用 controller
 * 借助于 spring cloud 微服务调用功能，注册中心是 Eureka
 * <p>
 * 当前 springboot-demo 项目是把自己当成了一个服务调用方，来调用下游微服务，具体动作：
 * 1.yml 中配置自己的服务名，和注册中心，服务启动将自己向注册中心注册
 * 2.配置 RestTemplate 启用 Ribbon 负载均衡，以此能通过服务名来调用下游微服务的接口，而不是 ip 来调用
 * 3.在业务代码中用 restTemplate 来调用下游微服务接口
 * <p>
 * 如果项目是微服务提供方如何配置？
 * 1.同样 springboot 项目中 yml 中配置服务名，以及注册中心，和目前此项目中配置一样
 * 2.编写微服务，其实就是 controller 上没带映射路径。启动后 user-service 已注册
 * @RestController
 * public class UserController {
 *     @GetMapping("/users")
 *     public String getUsers() {
 *         return "用户列表"; // 实际返回数据
 *     }
 * }
 */
@RestController
@RequestMapping("/microservice")
public class MicroServiceController {
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 其中进行了远程调用
     * <p>
     * 你的Web服务启动后，会连接注册中心（Eureka），来自 yml 中的配置。spring.application.name必须设置：Eureka会把它当作一个"消费者实例"，用它来指明调用方是谁，
     * 当你调用http://user-service/users时，Eureka会告诉你user-service的实例列表，（比如192.168.1.100:8080），然后自动选择一个调用。
     */
    @GetMapping("/invoke")
    public String invoke() {
        // 使用服务名"user-service"，而不是IP:端口，来访问 user-service 用户服务中的 users 接口
        return restTemplate.getForObject("http://user-service/users", String.class);
    }

}
