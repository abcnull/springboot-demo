package org.example.springbootdemo.init;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class InitService1 {
    // @PostConstruct
    // 在 Bean 初始化完成后立即执行，适用于单个 Bean 的初始化
    @PostConstruct
    public void init() {
        System.out.println("这里执行初始化逻辑，在 Bean 初始化完成后立即执行，适用于单个 Bean 的初始化");
    }
}
