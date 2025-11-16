package org.example.springbootdemo.config.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 日志记录 Filter，用于记录请求的基本信息和处理耗时，便于调试和性能分析
 *
 * 注解 @WebFilter 为 servlet 提供非 springboot 提供，配合 springboot 启动类加上 @ServletComponentScan 使其生效
 * Servlet 3.0+ 才可用此注解 @WebFilter
 * Spring Boot 2.x 对应 Tomcat 9.x，支持 Servlet 4.0
 * Spring Boot 3.x 对应 Tomcat 10.x，支持 Servlet 5.0
 */
@WebFilter
public class LoggingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        System.out.println("LoggingFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 在请求到达 Controller 之前执行的
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        long startTime = System.currentTimeMillis(); // startTime
        System.out.println("【请求开始】" + httpRequest.getMethod() + " " + httpRequest.getRequestURI());

        // 执行后续的过滤器或 Controller
        filterChain.doFilter(servletRequest, servletResponse);

        // 在响应返回客户端之前执行的代码
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("【请求结束】耗时：" + duration + "ms");
    }

    @Override
    public void destroy() {
        System.out.println("LoggingFilter destroyed");
        Filter.super.destroy();
    }
}
