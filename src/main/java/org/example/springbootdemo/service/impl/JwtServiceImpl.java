package org.example.springbootdemo.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.example.springbootdemo.service.IJwtService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * jwt service
 */
@Service
public class JwtServiceImpl implements IJwtService {
    // 密钥
    private final String secret = "your_strong_secret_key_here_1234567890";

    // 有效期默认 1 小时
    private final long expiration = 3600000;

    // 生成 JWT Token
    // 在 login 登录接口会用到，登录成功来生成 jwt token 返回前端，前端会自己保存到 localstorage 中
    public String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder().setClaims(claims)
                .setSubject(userName) // 传入 用户名
                .setIssuedAt(new Date(System.currentTimeMillis())) // 当前时间
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // 设置过期时间
                .signWith(SignatureAlgorithm.HS512, secret) // 使用 HS512 签名算发，安全且高效
                .compact();
    }

    // 验证 Token 是否有效
    // 后续每个请求，会通过自己编写的拦截起，拦截器中来校验
    public boolean validateToken(String token) {
        try {
            // 传入 token，识别其是否有效
            Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 从 Token 获取用户名
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
