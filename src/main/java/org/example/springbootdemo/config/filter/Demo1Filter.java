package org.example.springbootdemo.config.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * Demo1Filter 示例过滤器 1
 */
public class Demo1Filter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        System.out.println("Demo1Filter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Demo1Filter before request");
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("Demo1Filter after request");
    }

    @Override
    public void destroy() {
        System.out.println("Demo1Filter destroy");
        Filter.super.destroy();
    }
}
