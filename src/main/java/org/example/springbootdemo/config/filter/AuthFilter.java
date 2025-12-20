package org.example.springbootdemo.config.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限认证 Filter，用于校验请求头中的 Token，判断用户是否登录
 *
 * 注解 @WebFilter 为 servlet 提供非 springboot 提供，配合 springboot 启动类加上 @ServletComponentScan 使其生效
 * Servlet 3.0+ 才可用此注解 @WebFilter
 * Spring Boot 2.x 对应 Tomcat 9.x，支持 Servlet 4.0
 * Spring Boot 3.x 对应 Tomcat 10.x，支持 Servlet 5.0
 */
@WebFilter
public class AuthFilter implements Filter {
    // 假设的合法 Token
    private static final String VALID_TOKEN = "your-secret-token";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        System.out.println("AuthFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        // 获取请求头 Authorization
        String token = httpRequest.getHeader("Authorization");

        // 判断身份 token 是否合法
        if (VALID_TOKEN.equals(token)) {
            // Token 合法，放行请求，执行后续的过滤器或 Controller
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
//            // Token 无效，直接返回 401 未授权
//            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or missing token");

            // 这里为了整个项目不受影响，把上方 ⬆️ 注释掉了，直接给通过了
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {
        System.out.println("AuthFilter destroyed");
        Filter.super.destroy();
    }
}
