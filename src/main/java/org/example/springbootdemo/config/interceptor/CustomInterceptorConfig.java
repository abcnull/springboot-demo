package org.example.springbootdemo.config.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器注册
 *
 * 拦截器中推荐做：
 * 1.权限拦截，权限验证通常需要访问 Spring 容器中的 Bean（如用户认证服务、权限校验工具），拦截器可以直接注入这些依赖
 */
@Configuration
public class CustomInterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private AuthInterceptor authInterceptor;
    @Autowired
    private LoggingInterceptor loggingInterceptor;

    //将拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 AuthInterceptor
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**") // 拦截所有的 url
                .excludePathPatterns("/user/login") // 排除url: /user/login (登录)
                .excludePathPatterns("/user/reg") // 排除url: /user/reg   (注册)
                .excludePathPatterns("/image/**") // 排除 image(图像) 文件夹下的所有文件
                .excludePathPatterns("/**/*.js") // 排除任意深度目录下的所有".js"文件
                .excludePathPatterns("/**/*.css") // 排除任意深度目录下的所有".css"文件
                .order(1); // 数值越小优先级越高

        // 注册 LoggingInterceptor
        registry.addInterceptor(loggingInterceptor)
                .addPathPatterns("/**")
                .order(2);
    }
}
