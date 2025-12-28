package org.example.springbootdemo.controller.jwt;

import org.example.springbootdemo.service.IJwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * jwt controller
 * <p>
 * jwt 登录认证，基于 token 的登录认证，无状态，可扩展
 * 比如一个 web 平台登录
 */
@RestController
@RequestMapping("/jwt")
public class JwtController {
    @Autowired
    private IJwtService jwtService;

    /**
     * login 平台登录接口
     * 进行账密校验，成功则生成 token 返回前端，前端把 token 保存到 localstorage 中
     * 通过 AuthInterceptor 拦截器来拦截每个请求，通过拦截器获取 HttpServletRequest 中的 req header 中的 token，再来验证 token 是否有效，来决定让请求是否继续执行
     *
     * 使用 jwt 3 步：
     * 1.编写一个可被 spring 容器管理的 jwt util 工具类（我这里写的诗 jwt service，逻辑一样的），来实现 jwt 的 token 生成，以及 token 检验
     * 2.然后编写一个被 spring 容器管理的 auth 拦截器，来拦截请求，在请求之前通过 HttpServletRequest 获取请求头中的 token，并且做校验，成功则放行，否则返回
     * 3.把这个拦截器注册进行注册，其中会配置拦截哪些路径
     */
    @PostMapping("/login")
    public String login() {
        String userName = "xiaoming";
        String pwd = "123";
        boolean flag = false;
        // 账户密码与数据库的账号密码做比较，校验是否正确，如果比对正常，则 flag = true，否则 flag = false

        if (!flag) {
            // 如果用户密码验证不对，则返回提示
        }

        // 账号密码如果正确生成 jwt
        String token = jwtService.generateToken(userName);

        // 响应结果返回 Token（前端需存到 localStorage）
        return token;
    }
}
