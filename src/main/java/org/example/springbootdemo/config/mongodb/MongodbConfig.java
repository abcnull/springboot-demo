/*
package org.example.springbootdemo.config.mongodb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import java.util.Optional;

*/
/**
 * mongodb config
 *
 * document 中时间字段 @CreatedDate/@LastModifiedDate 只是 Spring Data 中的标记注解，
 * Spring Data 需要通过 @EnableMongoAuditing 来启用 AOP 拦截，在保存和更新操作时，Spring 会自动填充这些字段，这是 Spring AOP 和反射机制的体现
 *//*

// 配置类 - 必须启用审计功能
@Configuration
@EnableMongoAuditing  // 关键配置！没有这个注解，时间字段不会自动更新
public class MongodbConfig {
    // 也可以在这里自定义审计行为
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.of("System"); // 返回当前操作员信息
    }
}

// ⬆️ 因为没有 mongodb 服务端，如果 mongo 服务端 ok 可以开放这里
*/
