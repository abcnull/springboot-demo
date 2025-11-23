package org.example.springbootdemo.config.async;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 注解 @Async 对应的自定义线程池 Config，需要异步的方式使用上填写 @Async
 *
 * Springboot 中自定义线程池，来满足 @Async 的方法在开启异步执行时候，使用自定义的线程池
 * 因为 Springboot 中默认的线程池 SimpleAsyncTaskExecutor 太野蛮了，对于复杂场景容易导致 OOM
 *
 * 当你实现了 springboot 约定的 AsyncConfigurer 接口，实现其中 getAsyncExecutor() 方法，就等同于后续使用 @Async 时候告知其要使用这个线程池
 */
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {
    /**
     * 自定义线程池配置
     *
     * @return 自定义 java.util.concurrent.Executor
     */
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);       // 核心线程数（始终存在的线程数）
        executor.setMaxPoolSize(10);       // 最大线程数（最大能开启几线程）
        executor.setQueueCapacity(25);     // 队列容量（队列中最多有多少个阻塞等待执行的任务）
        executor.setThreadNamePrefix("CustomAsync-"); // 线程名前缀
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); // 拒绝策略
        executor.initialize();
        return executor;
    }
}
