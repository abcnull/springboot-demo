//package org.example.springbootdemo.config.security;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
//import org.springframework.security.oauth2.client.registration.ClientRegistration;
//import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
//import org.springframework.security.oauth2.client.registration.ClientRegistrations;
//import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
//import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
///**
// * security config
// * <p>
// * 第一步：未登录访问业务
// * browser：用户发送 GET /abc (无Cookie) --->
// * server：Security Filter 检测未认证 --->
// * *** ├─ 创建HttpSession (生成JSESSIONID)
// * *** ├─ 生成随机state防CSRF攻击
// * *** └─ 302重定向到：/oauth2/authorization/keycloak --->
// * browser：收到302，跳转到Keycloak登录页 (携带新生成的JSESSIONID Cookie)
// * <p>
// * 第二步：Keycloak认证
// * browser：在Keycloak页面输入用户名/密码 --->
// * Keycloak：验证凭证，生成授权码 --->
// * *** 302重定向到：/login/oauth2/code/keycloak?
// * ***     code=AUTHORIZATION_CODE&  // 一次性授权码
// * ***     state=原始state值        // 用于防CSRF
// * browser：自动跳转回业务应用 (携带原始JSESSIONID Cookie + 授权码参数)
// * <p>
// * 第三步：认证回调处理
// * browser：GET /login/oauth2/code/keycloak?code=AUTHORIZATION_CODE --->
// * server：Security Filter 处理回调：
// * *** ├─ 验证state参数 (匹配会话中存储的值)
// * *** ├─ 用授权码向Keycloak请求访问令牌
// * *** ├─ 获取用户信息
// * *** ├─ 创建认证对象
// * *** ├─ 存储到会话：session.setAttribute("SPRING_SECURITY_CONTEXT", authentication)
// * *** └─ 302重定向回原始请求：/abc --->
// * browser：自动跳转到 /abc (携带已认证的JSESSIONID)
// * <p>
// * 第四步：已认证访问业务
// * browser：GET /abc (携带JSESSIONID=ABC123) --->
// * server：Security Filter 从会话恢复认证：
// * *** ├─ session.getAttribute("SPRING_SECURITY_CONTEXT")
// * *** ├─ SecurityContextHolder.setContext(context)
// * *** └─ 放行请求到业务逻辑 --->
// * server：执行业务逻辑，返回200响应 --->
// * browser：显示业务页面
// * <p>
// * 总体步骤：
// * browser => biz server => browser 最后浏览器重定向认证中心登录页
// * browser => 认证中心 => bowser 用户输入完密码，最后浏览器重定向到 /login/oauth2/code/keycloak 且携带登录成功后到授权码
// * browser => biz server => browser 访问 /login/oauth2/code/keycloak，鉴权信息被存储到业务服务端的会话中，并最后让浏览器重定向原来要请求的业务接口
// * browser => biz server => browser 最后浏览器重定向到 /abc 并得到业务响应结果
// * <p>
// * 会话由服务器维护，通过 JSESSIONID cookie识别，即使100个HTTP连接先后关闭，只要带着相同的JSESSIONID，就是同一个会话
// * 一个用户一个会话，会话可以设置过期时间或者长时间过期
// * 每次用户携带 Cookie: JSESSIONID=ABC123 来时系统会从会话中查询用户相关信息
// */
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//    @Value("${spring.security.oauth2.client.provider.keycloak.issuer-uri}")
//    private String issuerUri;
//
//    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
//    private String clientId;
//
//    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
//    private String clientSecret;
//
//    @Value("${spring.security.oauth2.client.registration.keycloak.scope}")
//    private String keycloakScope;
//
//    // 怕受到影响导致项目无法启动，这里正常的 @Bean 需要给注释掉，但是此 sso 需要开放这个 bean
////    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests(auth -> auth // 设置哪些请求要经过认证
//                        .requestMatchers(new AntPathRequestMatcher("/"), new AntPathRequestMatcher("/public/**")).permitAll()  // 公共资源
//                        .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasRole("ADMIN")   // 管理员资源
//                        .requestMatchers(new AntPathRequestMatcher("/user/**")).hasAnyRole("USER", "ADMIN") // 用户资源
//                        .anyRequest().authenticated()  // 其他请求都需要认证
//                )
//                /** 关键：设置如果请求过来时没有登录态，需要重定向的地方，然后该过滤器把请求打回，让浏览器重定向到 /oauth2/authorization/keycloak，它是认证中心 http://auth.example.com/auth/realms/my-realm 的触发 **/
//                .oauth2Login(oauth2 -> oauth2 // 设置登录页面
//                        .loginPage("/oauth2/authorization/keycloak")  // 自定义登录页面
//                        .userInfoEndpoint(userInfo -> userInfo
//                                /** 关键：当用户 SSO 登录后携带着授权码来时，过滤器从认证中心校验完成即认为该用户是合法用户后，将该用户信息封装到 KeycloakOAuth2User 中 **/
//                                .customUserType(KeycloakOAuth2User.class, "keycloak")  // 这里告诉 Spring Security 使用这个自定义类处理用户信息
//                        )
//                )
//                .logout(logout -> logout // 设置登出
//                        /** 关键：设置如果登出时，需要重定向的地方，然后该过滤器把请求打回，让浏览器重定向 **/
//                        .logoutSuccessHandler(oidcLogoutSuccessHandler())  // OIDC全局登出，这里会配置登出时候重定向到 http://auth.example.com/auth/realms/my-realm
//                )
//                .csrf(csrf -> csrf // 设置 CSRF 保护
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())  // CSRF保护
//                );
//
//        return http.build();
//    }
//
//    // 配置 OIDC 全局登出
//    @Bean
//    public LogoutSuccessHandler oidcLogoutSuccessHandler() {
//        OidcClientInitiatedLogoutSuccessHandler logoutSuccessHandler =
//                new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository());
//
//        // 设置登出后重定向URL
//        logoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}");
//
//        return logoutSuccessHandler;
//    }
//
//    // 配置 Keycloak 客户端注册
//    @Bean
//    public ClientRegistrationRepository clientRegistrationRepository() {
//        // 从配置中创建 ClientRegistration
//        ClientRegistration clientRegistration = ClientRegistrations
//                .fromOidcIssuerLocation(issuerUri)
//                .clientId(clientId)
//                .clientSecret(clientSecret)
//                .scope(keycloakScope.split(","))
//                .build();
//
//        return new InMemoryClientRegistrationRepository(clientRegistration);
//    }
//}
//
//// ⬆️ 因为 SSO 授权中心没有启动，所以注释掉这里，当启动后再开放
