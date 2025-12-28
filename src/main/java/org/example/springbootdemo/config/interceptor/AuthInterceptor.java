package org.example.springbootdemo.config.interceptor;

import org.example.springbootdemo.service.IJwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户认证拦截器。验证用户是否登录或 Token 是否有效，常用于 JWT 或 Session 认证
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private IJwtService jwtService;

    // 调用目标方法之前执行的方法
    // 如果返回 ture 表示拦截器验证成功，执行目标方法
    // 如果返回 false 表示拦截器验证失败，不再继续执行后续业务
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 排除非 Controller 请求
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // 获取 Token
        // 一般前端塞入请求头 Authorization 中，格式一般是 Bearer xxxxx
        String token = request.getHeader("Authorization");

        // 如果 Token 无效返回 false，不再执行后续业务接口方法
        if (token == null || !isValidToken(token)) {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "未授权");
//            return false;

            // 为了保障项目正常运行，把 ⬆️ 注释掉了

            return true;
        }

        return true;
    }

    // 该方法在控制器处理请求方法调用之后、解析视图之前执行，可以通过此方法对请求域中的模型和视图做进一步修改\
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    // 该方法在视图渲染结束后执行，可以通过此方法实现资源清理、记录日志信息等工作
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    // 校验 token 是否有效
    private boolean isValidToken(String token) {
        // 实际项目中调用 JWT 工具类验证 Token

        // ⬇️ 为了保证此项目正常运转，把下面这块注释掉了。实际中 jwt 验证通过下面代码来验证 token 是否合法
//        return jwtService.validateToken(token);

        return true; // 简化示例
    }
}
