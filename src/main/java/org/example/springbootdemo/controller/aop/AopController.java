package org.example.springbootdemo.controller.aop;

import org.example.springbootdemo.service.IAopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * aop controller
 * <p>
 * 面向切面编程 Aspect-Oriented Programming
 * <p>
 * AOP的核心价值
 * - 关注点分离：将横切关注点(日志、事务、安全等)与业务逻辑分离
 * - 代码复用：避免重复代码，一处定义，多处应用
 * - 非侵入性：不修改原有业务代码，通过配置即可增强功能
 * - 可维护性：集中管理横切逻辑，修改时只需调整切面代码
 */
@RequestMapping("/aop")
@RestController
public class AopController {
    @Autowired
    private IAopService aopService;


    /**
     * @LogOperation 注解在方法这里进行了切面，起到打印日志
     * 注意 OperationLogAspect 这个关键类，写了切面具体的操作逻辑
     */
    @RequestMapping("/log")
    public String logAop() {
        aopService.searchStudent("张三");
        return "success";
    }
}
