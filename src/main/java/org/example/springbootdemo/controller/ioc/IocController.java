package org.example.springbootdemo.controller.ioc;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ioc controller
 * <p>
 * IOC (Inversion of Control) 控制反转
 * <p>
 * 什么叫控制反转：
 * 在传统编程中，开发者控制对象的创建，而在 IOC 模式下，这个控制权被"反转"给了 Spring 容器，spring 容器来管控对象
 * 它不是具体的技术，而是一种设计原则，用来降低代码耦合度
 */
@RestController
@RequestMapping("/ioc")
public class IocController {

    @RequestMapping("/ioc")
    public String ioc() {
        /*
        @Autowired 依赖注入，实际就是将控制权交给容器

        @SpringBootApplication
        public class Application {
            public static void main(String[] args) {
                // 启动 IOC 容器
                ApplicationContext context = SpringApplication.run(Application.class, args);

                // 从容器获取 Bean（不再是new）
                UserService userService = context.getBean(UserService.class);
            }
        }

        @Bean  // 将方法返回值注册为 Bean 交给 spring 容器管理
        public JdbcTemplate jdbcTemplate(DataSource dataSource) {
            return new JdbcTemplate(dataSource); // 自动注入上面的dataSource
        }

        @Scope("singleton") // 默认单例
        @Scope("prototype") // 每次获取都创建新实例
         */
        return "success";
    }
}
