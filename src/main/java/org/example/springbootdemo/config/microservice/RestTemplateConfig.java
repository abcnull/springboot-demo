//package org.example.springbootdemo.config.microservice;
//
//import org.springframework.cloud.client.loadbalancer.LoadBalanced;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.client.RestTemplate;
//
///**
// * RestTemplate 配置类
// */
//@Configuration
//public class RestTemplateConfig {
//    @Bean
//    // 启用Ribbon负载均衡
//    // 它让RestTemplate能用服务名调用，而不是IP！
//    @LoadBalanced // 一定要加！
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }
//}
//
//// ⬆️ 因为 eureka 服务端没有启动，这里注释掉，否则会报错，当你存在 eureka 服务端，你这里要开放并做好配置
