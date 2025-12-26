package org.example.springbootdemo.config.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// 用户映射表
public class KeycloakOAuth2User implements OAuth2User {
    private final Map<String, Object> attributes;
    private final String name; // 当前用户

    private final List<GrantedAuthority> authorities;

    // 构造器
    public KeycloakOAuth2User(Map<String, Object> attributes, String nameAttributeKey) {
        this.attributes = attributes;
        this.name = (String) attributes.get(nameAttributeKey);

        // 从 Keycloak token 中提取角色
        List<String> roles = (List<String>) ((Map<String, Object>) attributes.get("realm_access")).get("roles");
        this.authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                .collect(Collectors.toList());
    }

    // 实现 OAuth2User 接口方法
    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
