package org.example.springbootdemo.config.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 注解 @Async 对应的自定义多种线程池 Config，需要异步的方式使用上填写 @Async("firstTaskExecutor") 或者 @Async("secondTaskExecutor")
 * Springboot 中自定义多种线程池，来满足 @Async("firstTaskExecutor") 的方法在开启异步执行时候，使用自定义的线程池
 *
 * 当你有多个线程池时，不需要实现 AsyncConfigurer，直接通过 @Bean 定义多个线程池，以后在 @Async 上指定名称即可
 */
@Configuration
@EnableAsync
public class MutiAsyncConfig {
    /**
     * 配置的第一个线程池
     *
     * @return firstTaskExecutor
     */
    @Bean(name = "firstTaskExecutor")
    public Executor orderTaskExecutor() {
        // 配置第一个线程池
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);       // 核心线程数
        executor.setMaxPoolSize(10);       // 最大线程数
        executor.setQueueCapacity(25);     // 队列容量
        executor.setThreadNamePrefix("FirstAsync-"); // 线程名前缀
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); // 拒绝策略
        executor.initialize();
        return executor;
    }

    /**
     * 配置的第一个线程池
     *
     * @return firstTaskExecutor
     */
    @Bean(name = "secondTaskExecutor")
    public Executor emailTaskExecutor() {
        // 配置第二个线程池
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);       // 核心线程数
        executor.setMaxPoolSize(20);       // 最大线程数
        executor.setQueueCapacity(50);     // 队列容量
        executor.setThreadNamePrefix("SecondAsync-"); // 线程名前缀
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); // 拒绝策略
        executor.initialize();
        return executor;
    }
}
