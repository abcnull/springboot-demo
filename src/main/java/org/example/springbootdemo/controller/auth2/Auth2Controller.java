package org.example.springbootdemo.controller.auth2;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * auth2.0 controller
 * 基于 oauth2.0 + security 常见于第三方登录，比如微信，微博，github 授权登录场景
 * <p>
 * 过程：
 * 1.【访问业务-重定向三方地址】：浏览器访问 /login → 业务返回，用户点登录按钮 → 微信授权页
 * 2.【访问三方-重定向业务security接口】：浏览器上用户授权后 → 第三方系统重定向回 http://your-domain.com/login/oauth2/code/wx?code=CODE 浏览器来展现。redirect-uri 来决定这个重定向的地址
 * 3.【访问业务-重定向三方登录后要跳转的地址】：浏览器访问 http://your-domain.com/login/oauth2/code/wx?code=CODE → 打到业务服务端，业务服务端 security 会自动处理它！此接口不需要进行 controller 层的实现！
 * * 3.1 访问 /login/oauth2/code/wx 后，发现 registrationId="wx"，从配置中取出 provider.wx 的参数
 * * 3.2 [code => access_token]: 用 code 参数向第三方平台 wx 请求（请求地址来自配置 token-uri），获取 access_token（无需你写 HTTP 客户端代码，自动处理）
 * * 3.3 [access_token => 用户信息]: 用 access_token 再向 wx 发送请求（请求地址来自配置 user-info-uri），来获取用户信息
 * 比如 wx 返回的用户信息：
 * {
 * "openid": "OPENID",
 * "nickname": "张三",
 * "sex": 1,
 * "province": "广东",
 * "city": "广州",
 * "country": "中国",
 * "headimgurl": "https://example.com/head.jpg",
 * "privilege": [],
 * "unionid": "UNIONID"
 * }
 * * 3.4 Spring Security 将用户信息（包含 openid）存入 OAuth2AuthorizedClient（所以后续业务接收的请求你在 controller 方法中 @RegisteredOAuth2AuthorizedClient("wx") OAuth2AuthorizedClient client 注入用户数据就能直接拿到实例）
 * * 3.5 Spring Security 将用户信息存入 SecurityContext 安全上下文中，通过 SecurityContextHolder 保存上下文
 * * 3.6 依据 Security2Config 的配置，发给浏览器让其重定向到 /user
 * * 3.7 通过 SecurityContextPersistenceFilter 将 SecurityContext 存储到 Session，同时，Spring Security 会设置一个 Cookie（默认名称：SESSION）到 HTTP 响应头：（Set-Cookie: SESSION=abc123xyz; Path=/; HttpOnly; Secure）
 * 4.【访问业务-返回业务结果】：浏览器其实这里拿到了 cookie（上一步响应头中的），以及重定向的 /user 地址（你配置的 /user），业务服务端接收请求
 * * 4.1 这个业务请求过来，其实已经带上了鉴权信息了，Security2Config 本质是一个拦截器的过滤链，其中的关键拦截器会处理权限的校验，校验不过返回 401 Unauthorized
 * <p>
 * <p>
 * 对应 application.yml 中配置如下，由于 yml 中已经配置来 sso 相关，所以没有配置上下面这块内容：
 */
/*
spring:
  security:
    oauth2:
      client:
        registration:
          wx:  # 自定义注册名（可任意，但需和代码一致）
            # wx 平台注册给你的
            client-id: your-wechat-appid  # 替换为你的微信AppID
            client-secret: your-wechat-appsecret  # 替换为你的微信AppSecret

            redirect-uri: "{baseUrl}/login/oauth2/code/wx"  # 回调地址（需在微信平台注册）：即第三方平台授权(登录)通过之后让浏览器重定向的地址

            # redirect-uri 这个接口打到业务服务端会带有 code，会请求 token-uri 地址，目的地是将 code 传入，为了拿到 token，传递到其中的一个入参
            authorization-grant-type: authorization_code

            scope: snsapi_login  # 微信固定Scope

            # redirect-uri 中尾部是 wx 的配置。当授权通过后，浏览器发送 redirect-uri 到业务服务端，获取 wx 相关数据，即下方
            provider:
              wx:
                authorization-uri: https://open.weixin.qq.com/connect/qrconnect
                # redirect-uri 这个接口打到业务服务端会带有 code，会请求这个地址，目的地是将 code 传入，为了拿到 token
                token-uri: https://api.weixin.qq.com/sns/oauth2/access_token
                # 获取 access token 后，用 access token 向 user-info-uri 发送请求，获取用户信息
                user-info-uri: https://api.weixin.qq.com/sns/userinfo
                # 向 user-info-uri 发送请求时，需要携带此信息
                user-name-attribute: openid  # 关键！微信返回的用户唯一标识字段
*/
@RestController
@RequestMapping("/auth2")
public class Auth2Controller {
    /**
     * login，返回一个微信授权接口页面让用户点击来跳转微信授权
     */
    @PostMapping("/login")
    public String login() {
        return "<a href=\"/oauth2/authorization/wx\">微信登录</a>";
    }

    /**
     * 第三方授权成功后，最后回调到这个业务接口
     */
    // 获取用户信息（@RegisteredOAuth2AuthorizedClient("wx") OAuth2AuthorizedClient authorizedClient 已经包含用户信息数据）
    @GetMapping("/user")
    public String userInfo(@RegisteredOAuth2AuthorizedClient("wx") OAuth2AuthorizedClient authorizedClient) {
        // 无需手动调用微信 API！Spring Security 已自动获取用户信息
        // 此处可直接用 authorizedClient 获取 access token 和用户数据

        return "登录成功！用户标识: " + authorizedClient.getPrincipalName();
    }
}
