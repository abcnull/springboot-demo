package org.example.springbootdemo.config.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 日志记录拦截器。记录请求耗时、请求路径、用户信息等，用于监控和调试
 */
@Component
public class LoggingInterceptor implements HandlerInterceptor {
    // 调用目标方法之前执行的方法
    // 如果返回 ture 表示拦截器验证成功，执行目标方法
    // 如果返回 false 表示拦截器验证失败，不再继续执行后续业务
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute("startTime", System.currentTimeMillis());

        // 配上 log 后用 log 记录
        System.out.printf("请求开始: %s %s\n", request.getMethod(), request.getRequestURI());

        return true;
    }

    // 该方法在控制器处理请求方法调用之后、解析视图之前执行，可以通过此方法对请求域中的模型和视图做进一步修改
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    // 该方法在视图渲染结束后执行，可以通过此方法实现资源清理、记录日志信息等工作
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        long duration = System.currentTimeMillis() - (Long) request.getAttribute("startTime");

        // 配上 log 后用 log 记录
        System.out.printf("请求结束: 耗时 %s ms, 响应状态 %s\n", duration, response.getStatus());
    }
}
