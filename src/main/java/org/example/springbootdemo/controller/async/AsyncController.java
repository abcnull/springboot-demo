package org.example.springbootdemo.controller.async;

import lombok.SneakyThrows;
import org.example.springbootdemo.service.IAsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

/**
 * Springboot 使用 @Async 的异步能力 Controller
 * <p>
 * 常用场景：
 * 1.用户注册（userThreadPool），比如注册时候，异步发送邮件，异步发送验证码
 * 2.邮件发送（emailThreadPool）
 * 3.短信通知（smsThreadPool）
 * 4.数据同步（syncThreadPool）
 * 5.报表生成（reportThreadPool）
 */
@RestController
@RequestMapping("/async")
public class AsyncController {
    @Autowired
    private IAsyncService asyncService;

    /**
     * 默认线程池的简单的方法异步，此方法会被异步执行，生产中不建议使用这种！
     * <p>
     * 首先 @EnableAsync：可在启动类或自定义线程池的配置类上上加，表示开启异步
     * 然后 @Async：在方法上加，标记为异步执行
     * <p>
     * 直接使用 Async，它其实是使用了默认的线程池 SimpleAsyncTaskExecutor，但是过于野蛮，对于低异步量的简单场景可以这么用，复杂一点高并发的场景这么用很容易 OOM，一般需要自定义线程池
     * <p>
     * 注意：此方式展示默认的 springboot 的 SimpleAsyncTaskExecutor 线程池，你需要把 org.example.springbootdemo.config.async.AsyncConfig 中的代码注释掉，此代码表示要给 @Async 配置一个自定义的线程池
     */
    @GetMapping("/basic")
    public String basicAsync() {
        asyncService.basicAsync();
        return "success";
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
    @GetMapping("/custom")
    public String customAsync() {
        asyncService.customAsync();
        return "success";
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
    @GetMapping("/custom-muti")
    public String customMutiAsync() {
        asyncService.customMutiAsync();
        return "success";
    }

    /**
     * 使用 @Async 异步的方法获取异步返回结果
     */
    @SneakyThrows
    @GetMapping("/resp-get")
    public String asyncRespGet() {
        // 异步执行，结果赋予 future，但是异步执行不会阻碍这个主线程执行
        CompletableFuture<String> future = asyncService.asyncRespGet();

        // 主线程执行，这里主线程执行，future 结果还没有拿到
        System.out.println("主线程继续执行...");

        // 这里 future.get() 是 CompletableFuture 的阻塞能力，这一行可以阻塞主线程直到拿到这个 future 的结果
        return future.get(); // 获取结果（会阻塞直到完成）
    }

    /**
     * 在使用 springboot 的 @Async 时候有哪些常见坑点
     */
    @GetMapping("/pitfalls")
    public String asyncPitFalls() {
        asyncService.asyncPitFalls();
        return "success";
    }
}
