package org.example.springbootdemo.config.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * filter 过滤器要么通过 @WebFilter + @ServletComponentScan 注解来实现注册使用
 * 要么通过此 @Configuration 和 @Bean 来注册过滤器使用，使用此种配置方式更灵活，可以灵活设置过滤器过滤的 url 以及过滤器的执行优先级
 *
 * 过滤器中推荐做：
 * 1.日志过滤，可覆盖所有请求，实现低侵入的性能评估
 * 2.cors 跨域处理
 */
@Configuration
public class CustomFilterConfig {
    @Bean
    public FilterRegistrationBean<Demo1Filter> demo1Filter() {
        FilterRegistrationBean<Demo1Filter> registration = new FilterRegistrationBean<>();

        registration.setFilter(new Demo1Filter());
        registration.addUrlPatterns("/*"); // 指定过滤器的 URL 模式
        registration.setOrder(2); // 设置 Filter 的执行顺序 2

        return registration;
    }

    @Bean
    public FilterRegistrationBean<Demo2Filter> demo2Filter() {
        FilterRegistrationBean<Demo2Filter> registration = new FilterRegistrationBean<>();

        registration.setFilter(new Demo2Filter());
        registration.addUrlPatterns("/api/*"); // 指定过滤器的 URL 模式
        registration.setOrder(1); // 设置 Filter 的执行顺序 1

        return registration;
    }
}
