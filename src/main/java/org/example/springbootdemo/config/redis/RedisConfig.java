package org.example.springbootdemo.config.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
//    // 代码设置 redis 连接池 生成 RedisConnectionFactory
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
//        config.setHostName("192.168.1.101");
//        config.setPort(6379);
//        config.setDatabase(0);
//
//        GenericObjectPoolConfig<?> poolConfig = new GenericObjectPoolConfig<>();
//        poolConfig.setMaxTotal(8);
//        poolConfig.setMaxIdle(8);
//        poolConfig.setMinIdle(2);
//        poolConfig.setMaxWaitMillis(1000);
//
//        LettucePoolingClientConfiguration poolingConfig = LettucePoolingClientConfiguration.builder()
//                .poolConfig(poolConfig)
//                .build();
//
//        return new LettuceConnectionFactory(config, poolingConfig);
//    }
//
//    // 代码设置 redis 连接使用的能力，生成 RedisTemplate
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        // 设置 redis 连接
//        template.setConnectionFactory(redisConnectionFactory);
//
//        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
//        // 设置使用 StringRedisSerializer 来序列化和反序列化 redis 的 key 值
//        template.setKeySerializer(stringRedisSerializer);
//        template.setHashKeySerializer(stringRedisSerializer);
//
//        // 使用 GenericJackson2JsonRedisSerializer 来序列化和反序列化 redis 的 value 值
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
//        template.setValueSerializer(jackson2JsonRedisSerializer);
//        template.setHashValueSerializer(jackson2JsonRedisSerializer);
//
//        template.afterPropertiesSet();
//        return template;
//    }
}