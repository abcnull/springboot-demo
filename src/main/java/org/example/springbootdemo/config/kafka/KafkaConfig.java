package org.example.springbootdemo.config.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

/**
 * 配置 kafka
 * 包括配置 kafka 的提交模式（手动提交偏移量来确认消息消费）
 *
 * 配置手动提交，注意 application.yml 中需要开启配置支持手动提交
 */
@Configuration
public class KafkaConfig {
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
//
//        factory.setConsumerFactory(consumerFactory());
//        factory.setConcurrency(3);
//
//        // 关键：设置为手动提交模式
//        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
//
//        return factory;
//    }
//
//    @Bean
//    public ConsumerFactory<String, String> consumerFactory() {
//        // 配置ConsumerFactory
//    }
}
