//package org.example.springbootdemo.config.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
///**
// * Security2Config 配置有第三方授权完成后让浏览器重定向的 /user 业务接口，以及配置哪些请求需要经过权限检测
// * <p>
// * 注解 @EnableWebSecurity 本质是一个拦截器的过滤链条
// */
//@Configuration
//@EnableWebSecurity
//public class Security2Config {
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.oauth2Login(oauth2 ->
//                        // 必须配置！表示 security 在访问 wx 接口获取用户信息成功后，告知用户浏览器需要重定向到 /user 接口
//                        oauth2.defaultSuccessUrl("/user", true))
//                .authorizeHttpRequests(auth ->
//                        // 关键，意味着 所有请求都必须通过认证，这样 security 中某个过滤器会进行接口的权限校验
//                        auth.anyRequest().authenticated());
//        return http.build();
//    }
//}
//
//// ⬆️ 这里注释掉了，因为已经写了 SSO 的示例，使用的是 SecurityConfig 文件，防止影响，这里注释掉了
