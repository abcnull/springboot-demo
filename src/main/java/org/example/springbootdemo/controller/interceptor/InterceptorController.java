package org.example.springbootdemo.controller.interceptor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 拦截器的使用
 *
 * 拦截器中推荐做：业务相关性大的
 * 1.权限拦截，权限验证通常需要访问 Spring 容器中的 Bean（如用户认证服务、权限校验工具），拦截器可以直接注入这些依赖
 */
@RestController
@RequestMapping("/interceptor")
public class InterceptorController {
    // 拦截器记录日志
    // @Component 拦截器 + @Configuration implements WebMvcConfigurer 注册拦截器
    @GetMapping("/logging")
    public String logging() {
        System.out.println("日志记录 Interceptor：用于记录请求的基本信息和处理耗时，便于调试和性能分析，查看 LoggingInterceptor");
        return "success";
    }

    // 拦截器控制请求访问权限
    // @Component 拦截器 + @Configuration implements WebMvcConfigurer 注册拦截器
    @GetMapping("/auth")
    public String auth() {
        System.out.println("权限认证 Interceptor，用于校验请求头中的 Token，判断用户是否有权限访问，查看 AuthInterceptor");
        return "success";
    }
}
