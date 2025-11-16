package org.example.springbootdemo.controller.init;

import org.example.springbootdemo.init.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 项目运行时，初始化数据的举例 todo：待测试
 * 一般建议选择 ApplicationRunner 或者监听 ApplicationReadyEvent 的方式
 */
@RestController
@RequestMapping("/init-data")
public class InitDataController {
    @Autowired
    private InitService1 initService1;
    @Autowired
    private InitService6 initService6;
    @Autowired
    private InitService5 initService5;
    @Autowired
    private InitService4 initService4;
    @Autowired
    private InitService2 initService2;
    @Autowired
    private InitService3 initService3;

    // 顺序：1
    // 单个 bean 初始化完后 @PostConstruct 对应的方法立即被执行
    @GetMapping("/on-bean-init")
    public String onBeanInit() {
        System.out.println("顺序1: InitService1 这个 component bean 在初始化完成时候，@PostConstruct 对应的方法被执行");
        return "success";
    }

    // 顺序 2
    // 通过 @Bean 声明来控制初始化
    @GetMapping("/bean-declare-init")
    public String beanDeclareInit() {
        System.out.println("顺序2: InitService6 中通过 @Bean 声明来控制方法中要执行的初始化逻辑");
        return "success";
    }

    // 顺序 3
    // 通过实现某个特定事件的监听器，来监听特定的事件，来初始化
    @GetMapping("/listen-special-event")
    public String listenSpecialEvent() {
        System.out.println("顺序3: InitService5 通过实现某个特定事件的监听器，来监听特定的事件，来初始化");
        return "success";
    }

    // 顺序 3
    // 通过 @EventListener 来监听标志性的 bean 就绪且应用完全启动的事件，来初始化
    @GetMapping("/listen-event")
    public String listenEvent() {
        System.out.println("顺序3: InitService4 通过 @EventListener 来监听标志性的 bean 就绪且应用完全启动的事件，来初始化");
        return "success";
    }

    // 顺序 4
    // 通过实现 CommandLineRunner 中的 run 方法来实现初始化，可以访问命令行参数 args
    @GetMapping("/command-line")
    public String implementsCommandLineRunner() {
        System.out.println("顺序4: initService2 通过实现 CommandLineRunner 中的 run 方法来实现初始化，可以访问命令行参数 args");
        return "success";
    }

    // 顺序 4
    // 通过实现 ApplicationRunner 中的 run 方法来实现初始化，可以访问命令行参数 args，参数更丰富
    @GetMapping("/application")
    public String implementsApplicationRunner() {
        System.out.println("顺序4: initService3 通过实现 ApplicationRunner 中的 run 方法来实现初始化，可以访问命令行参数 args，参数更丰富");
        return "success";
    }
}
