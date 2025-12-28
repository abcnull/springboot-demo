package org.example.springbootdemo.controller.sso;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * sso controller
 * 单点登录，多个平台/系统的统一登录，跨平台有效
 * 一般公司业务会有自己的 sso 认证中心，一般只需要在配置文件中配置上公司认证中心连接等内容即可，不需要使用购买的公有云
 * <p>
 * OAuth2.0/OpenID Connect：当前最主流方案，适合现代微服务架构，其被实现的具体产品有：
 * - Keycloak
 * - Auth0
 * - AWS Cognito
 * - Okta
 * - Azure AD
 * <p>
 * Keycloak 是一个开源的身份和访问管理(IAM)解决方案,提供了单点登录(SSO),它支持多种协议，包括OAuth2.0、OpenID Connect、SAML等，是企业级应用中常用的认证授权中心
 * - 初次访问：用户访问受保护的资源，如/user/profile
 * - 认证检查：Spring Security 检测到用户未认证
 * - 重定向到认证中心：用户被重定向到 Keycloak 登录页面
 * - 用户登录：用户在 Keycloak 界面上输入凭证
 * - 授权码返回：Keycloak 验证用户凭证后，重定向回应用，携带授权码
 * - 令牌交换：Spring Security 后台使用授权码向 Keycloak 请求访问令牌
 * - 用户信息获取：使用访问令牌获取用户信息
 * - 创建认证对象：基于用户信息创建Authentication对象
 * <p>
 * 第一步：未登录访问业务
 * browser：用户发送 GET /abc (无Cookie) --->
 * server：Security Filter 检测未认证 --->
 * *** ├─ 创建HttpSession (生成JSESSIONID)
 * *** ├─ 生成随机state防CSRF攻击
 * *** └─ 302重定向到：/oauth2/authorization/keycloak --->
 * browser：收到302，跳转到Keycloak登录页 (携带新生成的JSESSIONID Cookie)
 * <p>
 * 第二步：Keycloak认证
 * browser：在Keycloak页面输入用户名/密码 --->
 * Keycloak：验证凭证，生成授权码 --->
 * *** 302重定向到：/login/oauth2/code/keycloak?
 * ***     code=AUTHORIZATION_CODE&  // 一次性授权码
 * ***     state=原始state值        // 用于防CSRF
 * browser：自动跳转回业务应用 (携带原始JSESSIONID Cookie + 授权码参数)
 * <p>
 * 第三步：认证回调处理
 * browser：GET /login/oauth2/code/keycloak?code=AUTHORIZATION_CODE --->
 * server：Security Filter 处理回调：
 * *** ├─ 验证state参数 (匹配会话中存储的值)
 * *** ├─ 用授权码向Keycloak请求访问令牌
 * *** ├─ 获取用户信息
 * *** ├─ 创建认证对象
 * *** ├─ 存储到会话：session.setAttribute("SPRING_SECURITY_CONTEXT", authentication)
 * *** └─ 302重定向回原始请求：/abc --->
 * browser：自动跳转到 /abc (携带已认证的JSESSIONID)
 * <p>
 * 第四步：已认证访问业务
 * browser：GET /abc (携带JSESSIONID=ABC123) --->
 * server：Security Filter 从会话恢复认证：
 * *** ├─ session.getAttribute("SPRING_SECURITY_CONTEXT")
 * *** ├─ SecurityContextHolder.setContext(context)
 * *** └─ 放行请求到业务逻辑 --->
 * server：执行业务逻辑，返回200响应 --->
 * browser：显示业务页面
 * <p>
 * 总体步骤：
 * browser => biz server => browser 最后浏览器重定向认证中心登录页
 * browser => 认证中心 => bowser 用户输入完密码，最后浏览器重定向到 /login/oauth2/code/keycloak 且携带登录成功后到授权码
 * browser => biz server => browser 访问 /login/oauth2/code/keycloak，鉴权信息被存储到业务服务端的会话中，并最后让浏览器重定向原来要请求的业务接口
 * browser => biz server => browser 最后浏览器重定向到 /abc 并得到业务响应结果
 * <p>
 * 会话由服务器维护，通过 JSESSIONID cookie识别，即使100个HTTP连接先后关闭，只要带着相同的JSESSIONID，就是同一个会话
 * 一个用户一个会话，会话可以设置过期时间或者长时间过期
 * 每次用户携带 Cookie: JSESSIONID=ABC123 来时系统会从会话中查询用户相关信息
 */
@RequestMapping("/sso")
@RestController
public class SsoController {
    @GetMapping("/")
    public String home(Model model) {
        // model.addAttribute 添加的数据确实在 Java 代码中"消失"了，但它并没有真正消失——它被框架隐式传递给了视图模板
        model.addAttribute("title", "欢迎使用单点登录系统");
        // 在这里 model 被传递给了视图模版
        // Spring MVC 框架捕获这个返回值，自动将 Model 中的所有属性 传递给该视图，视图引擎（如 Thymeleaf）在渲染时使用这些数据
        return "home"; // 对应 src/main/resources/templates/home.html
    }

    // @AuthenticationPrincipal OAuth2User principal 它从安全上下文中获取数据，而安全上下文存储在会话中
    @GetMapping("/user/profile")
    public String userProfile(Model model, @AuthenticationPrincipal OAuth2User principal) {
        model.addAttribute("userName", principal.getName());
        model.addAttribute("userAttributes", principal.getAttributes());
        return "user/profile"; // 对应 src/main/resources/templates/user/profile.html
    }

    // 全局登出端点，确保清除会话
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/logout"; // 重定向到 Spring Security 的登出端点
    }
}
