package org.example.springbootdemo.service.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.springbootdemo.service.IAsyncService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * springboot 异步 Serivce
 */
@Slf4j
@Service
public class AsyncServiceImp implements IAsyncService {
    /**
     * 默认线程池的简单的方法异步，此方法会被异步执行，生产中不建议使用这种！
     * <p>
     * 首先 @EnableAsync：可在启动类上加，表示开启异步
     * 然后 @Async：在方法上加，标记为异步执行
     * 它使用了默认的线程池 SimpleAsyncTaskExecutor，但是过于野蛮，对于低异步量的简单场景可以这么用，复杂一点高并发的场景这么用很容易 OOM，一般需要自定义线程池
     * <p>
     * 注意：此方式展示默认的 springboot 的 SimpleAsyncTaskExecutor 线程池，你需要把 org.example.springbootdemo.config.async.AsyncConfig 中的代码注释掉，此代码表示要给 @Async 配置一个自定义的线程池
     */
    @Async
    @SneakyThrows
    @Override
    public void basicAsync() {
        System.out.println("basicAsync 发送文件中...");
        Thread.sleep(2000); // 模拟耗时操作
        System.out.println("basicAsync 发送文件结束");
    }

    /**
     * 自定义线程池的方法异步，此方法会被异步执行，生产建议使用这种方式！
     * <p>
     * 首先 @EnableAsync：在线程池配置类上加（建议最好不要在启动类上加！），表示开启异步
     * 然后 @Async：在方法上加，标记为异步执行
     * 它使用了自定义线程池
     * <p>
     * 注意：此方式展示自定义的线程池，org.example.springbootdemo.config.async.AsyncConfig 中的代码起到作用，此代码表示要给 @Async 配置一个自定义的线程池
     */
    @Async
    @SneakyThrows
    @Override
    public void customAsync() {
        System.out.println("customAsync 发送文件中...");
        Thread.sleep(2000); // 模拟耗时操作
        System.out.println("customAsync 发送文件结束");
    }

    /**
     * 自定义多种不同的线程池的方法异步，此方法会被异步执行，生产中如果配置多种不同线程池的需求时可用！
     * <p>
     * 首先 @EnableAsync：在线程池配置类上加（建议最好不要在启动类上加！），表示开启异步
     * 然后 @Async(线程池名)：在方法上加，标记为异步执行
     * 它使用了自定义多种不同的线程池
     * <p>
     * 注意：此方式展示自定义多种不同的线程池，org.example.springbootdemo.config.async.MutiAsyncConfig 中的代码起到作用，此代码表示要给 @Async(线程池名) 配置一个自定义的线程池
     */
    @Async("firstTaskExecutor") // 指定使用线程池
    @SneakyThrows
    @Override
    public void customMutiAsync() {
        System.out.println("配置的第一个线程池发送文件中...");
        Thread.sleep(2000); // 模拟耗时操作
        System.out.println("配置的第一个线程池发送文件结束");
    }

    /**
     * 使用 @Async 异步的方法获取异步返回结果
     *
     * @return CompletableFuture<String>
     */
    @Async
    @SneakyThrows
    @Override
    public CompletableFuture<String> asyncRespGet() {
        // 耗时操作
        Thread.sleep(1000);

        return CompletableFuture.completedFuture("Result");
    }

    /**
     * 在使用 springboot 的 @Async 时候有哪些常见坑点
     */
    @Override
    public void asyncPitFalls() {
        // 坑点 1: 同一个类中使用该异步方法，并不会触发异步，必须通过 Spring 容器获取 Bean 调用
        this.basicAsync();
        log.info("asyncPitFalls, 同一个类中使用该异步方法，并不会触发异步，必须通过 Spring 容器获取 Bean 调用");

        // 坑点 2: static 方法不能用 @Async：Spring 的代理机制无法处理 static 方法
        log.info("asyncPitFalls, static 方法不能用 @Async：Spring 的代理机制无法处理 static 方法");

        // 坑点 3: 默认线程池太危险：不要在生产环境用默认线程池，会 OOM 的！
        log.info("asyncPitFalls, 默认线程池太危险：不要在生产环境用默认线程池，会 OOM 的！");
    }
}
