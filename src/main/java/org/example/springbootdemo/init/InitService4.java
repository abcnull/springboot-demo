package org.example.springbootdemo.init;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class InitService4 {
    // @EventListener 监听 ApplicationReadyEvent 事件
    // 确保所有 bean 就绪且应用完全启动，再执行初始化
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        System.out.println("确保所有 bean 就绪且应用完全启动，再执行初始化");
    }
}
