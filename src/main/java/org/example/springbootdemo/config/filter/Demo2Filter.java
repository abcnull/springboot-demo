package org.example.springbootdemo.config.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * Demo2Filter 示例过滤器 2
 */
public class Demo2Filter implements Filter  {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        System.out.println("Demo2Filter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Demo2Filter before request");
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("Demo2Filter after request");
    }

    @Override
    public void destroy() {
        System.out.println("Demo2Filter destroy");
        Filter.super.destroy();
    }
}
