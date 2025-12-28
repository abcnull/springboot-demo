package org.example.springbootdemo.config.microservice;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate 配置类
 */
@Configuration
public class RestTemplateConfig {
    @Bean
    // 启用Ribbon负载均衡
    // 它让RestTemplate能用服务名调用，而不是IP！
    @LoadBalanced // 一定要加！
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
