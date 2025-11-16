package org.example.springbootdemo.init;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class InitService5 implements ApplicationListener<ContextRefreshedEvent> {
    // 实现 ApplicationListener 接口
    // 通过监听特定生命周期事件：ContextRefreshedEvent，来执行初始化
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("通过监听特定生命周期事件：ContextRefreshedEvent，来执行初始化");
    }
}
