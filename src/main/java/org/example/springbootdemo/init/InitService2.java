package org.example.springbootdemo.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
public class InitService2 implements CommandLineRunner {
    // 通过实现 CommandLineRunner 接口
    // 应用启动后执行，可访问命令行参数
    @Override
    public void run(String... args) throws Exception {
        System.out.println("初始化，应用启动后执行，可访问命令行参数");
    }
}
