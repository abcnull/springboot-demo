package org.example.springbootdemo.controller.filter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 过滤器的使用
 *
 * 过滤器中推荐做：非业务相关的
 * 1.日志过滤，可覆盖所有请求，实现低侵入的性能评估
 * 2.cors 跨域处理
 */
@RestController
@RequestMapping("/filter")
public class FilterController {
    // 过滤器记录日志
    // @WebFilter servlet 注解 + @ServletComponentScan 扫描，来注册过滤器
    @GetMapping("/logging")
    public String logging() {
        System.out.println("日志记录 Filter：用于记录请求的基本信息和处理耗时，便于调试和性能分析，查看 LoggingFilter");
        return "success";
    }

    // 过滤器控制请求访问权限
    // @WebFilter servlet 注解 + @ServletComponentScan 扫描，来注册过滤器
    @GetMapping("/auth")
    public String auth() {
        System.out.println("权限认证 Filter，用于校验请求头中的 Token，判断用户是否登录，查看 AuthFilter");
        return "success";
    }

    // 过滤器控制 cors 跨域请求
    // @WebFilter servlet 注解 + @ServletComponentScan 扫描，来注册过滤器
    @GetMapping("/cors")
    public String cors() {
        System.out.println("跨域处理 Filter（CORS Filter），解决前后端分离项目中的跨域问题，设置 CORS 相关响应头，查看 CorsFilter");
        return "success";
    }

    // 另外一种更详细常用的过滤器注册配置方式
    // @Configuration + @Bean 来注册过滤器，并且进行详细配置
    @GetMapping("/config")
    public String config() {
        System.out.println("filter 过滤器要么通过 @WebFilter + @ServletComponentScan 注解来实现注册使用");
        System.out.println("要么通过此 @Configuration 和 @Bean 来注册过滤器使用，使用此种配置方式更灵活，可以灵活设置过滤器过滤的 url 以及过滤器的执行优先级");
        return "success";
    }
}
