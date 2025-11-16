package org.example.springbootdemo.init;

import org.example.springbootdemo.model.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitService6 {
    // 通过 @Bean 声明控制来初始化
    @Bean(initMethod = "initName")
    public Student init() {
        return new Student();
    }
}
