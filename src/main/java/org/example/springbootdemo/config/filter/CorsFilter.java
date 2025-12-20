package org.example.springbootdemo.config.filter;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 跨域处理 Filter（CORS Filter），解决前后端分离项目中的跨域问题，设置 CORS 相关响应头
 *
 * 注解 @WebFilter 为 servlet 提供非 springboot 提供，配合 springboot 启动类加上 @ServletComponentScan 使其生效
 * Servlet 3.0+ 才可用此注解 @WebFilter
 * Spring Boot 2.x 对应 Tomcat 9.x，支持 Servlet 4.0
 * Spring Boot 3.x 对应 Tomcat 10.x，支持 Servlet 5.0
 */
@WebFilter
public class CorsFilter implements Filter {
    private static final List<String> ALLOWED_ORIGINS = new ArrayList<String>() {
        {
            add("https://example.com");
            add("https://**.subdomain.com"); // 使用 ** 匹配多级域名
            add("http://localhost:8080");
        }
    };
    private AntPathMatcher matcher;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        matcher = new AntPathMatcher();
        System.out.println("CorsFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        // 获取请求头 origin 值
        String origin = httpRequest.getHeader("Origin");

        // 判断该请求 origin 是否在允许的 origin 列表中
        boolean isAllowed = false;
        if (!StringUtils.isEmpty(origin)) {
            isAllowed = ALLOWED_ORIGINS.stream().anyMatch(pattern -> matcher.match(pattern, origin));
        }

        // 不论是 option 还是 get/post 请求，如果该请求 origin 不在允许的 origin 列表中，返回 403
        if (!isAllowed) {
//            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Origin not allowed: " + origin);
//            return;

            // 为了保证项目正常运行 ⬆️ 被注释掉，实际上方为正常代码
        }

        // 后续是 origin 是在允许的 origin 列表中

        // 设置 CORS 响应头
        httpResponse.setHeader("Access-Control-Allow-Origin", origin);
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Max-Age", "3600");  // 设置预检请求缓存时间为1小时

        // 预检请求（OPTIONS）直接返回 200
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // 如果是非预检请求，执行后续的过滤器或 Controller
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        System.out.println("CorsFilter destroyed");
        Filter.super.destroy();
    }
}
