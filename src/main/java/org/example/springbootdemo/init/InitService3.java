package org.example.springbootdemo.init;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class InitService3 implements ApplicationRunner {
    // 通过实现 ApplicationRunner 来实现初始化
    // 类似 CommandLineRunner 但提供更丰富的 ApplicationArguments 参数
    // 启动参数 --key=value 形式
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("执行初始化，类似 CommandLineRunner 但提供更丰富的 ApplicationArguments 参数");
    }
}
