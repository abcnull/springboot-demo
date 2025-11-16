package org.example.springbootdemo.controller.log;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.springbootdemo.model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * springboot 日志相关使用
 * <p>
 * springboot 默认使用 logback，其日志框架在 spring-boot-starter 中的 spring-boot-starter-logging，所以无需额外引入其他依赖，这里以演示 logback 配置接入为主
 * 如果想使用 log4j2 日志框架，则需要先排除 Logback，再引入 spring-boot-starter-log4j2
 */
@RestController
@RequestMapping("/log")
@Slf4j
public class LogController {
    // @Slf4j 或者下面这种形式选择使用一种方式即可，分别对应 log.info 和 logger.info 写法
    // 为什么需要是 static？虽然去掉也可用但最好 static，因为希望 logger 是类级别共享而不是实例级别共享，静态变量在类加载时初始化，且只会创建一次，避免了每次实例化类时重复调用
    private final static Logger logger = LoggerFactory.getLogger(LogController.class);

    // logback 日志配置：通过文件配置 logback 日志，src/main/resources/logback-spring.xml 配置 logback-spring.xml
    @GetMapping("/logback-spring")
    public String logbackSpring() {
        // springboot 默认使用 logback，其日志框架在 spring-boot-starter 中的 spring-boot-starter-logging，所以无需额外引入其他依赖
        // springboot 项目会尝试加载 resources 下的 logback-spring.xml 和 logback.xml 文件，如果二者都不存在，则使用内置的默认 Logback 配置。logback-spring.xml > logback.xml 查找文件优先级
        // application.yml 直接可指定 logback xml 日志配置文件路径
        System.out.println("application.yml 中配置 logging 配置，指定 src/main/resources/logback-spring.xml 位置，xml 中详细配置日志");
        return "success";
    }

    // logback 日志配置：通过 application.yml 直接通过 logging 做 logback 日志简单配置
    @GetMapping("/logback-yml")
    public String logbackYml() {
        // springboot 默认使用 logback，其日志框架在 spring-boot-starter 中的 spring-boot-starter-logging，所以无需额外引入其他依赖

        /*
            logging:
              level:
                root: INFO
                com.yourpackage: DEBUG # 设置特定包的日志级别
              file:
                name: server.log # 指定日志文件的名称
              pattern:
                console: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n" # 设置控制台日志输出格式
              logback:
                rollingpolicy:
                  max-file-size: 10MB # 设置日志文件滚动的大小限制
                  file-name-pattern: server.%d{yyyy-MM-dd}.%i.log # 设置滚动日志文件的命名模式
        * */
        System.out.println("application.yml 中直接配置 logging，一般使用简单的 logback 日志配置，一般还是建议文件中配置");
        return "success";
    }

    // logback 异步日志输出，默认是同步会阻塞主业务
    @GetMapping("/logback-async")
    public String logbackAsync() {
        /*
            如果不添加 AsyncAppender 默认是同步的
            AsyncAppender 会启动一个独立的后台线程
            当你调用 logger.info() 时，它只是把日志事件（LoggingEvent）放入一个队列，然后立即返回，不阻塞业务线程
            后台线程从队列中取出日志，并交给真正的 Appender（如 File、Console）去写
        * */
        System.out.println("logback 默认同步日志会阻塞线上高并发场景会阻塞主线程，线上建议设置异步日志");
        System.out.println("queueSize 表示异步日志队列大小，discardingThreshold 表示放弃低级别日志时的阈值（用默认即可），includeCallerData 表示是否包含调用信息线上需要使用 false！maxFlushTime 表示应用关闭时最长等待多少 ms 让日志写入，线上不应使用默认值 0");
        System.out.println("具体看 logback-spring.xml 配置");
        return "success";
    }

    // log4j2 日志配置：springboot 使用 log4j2
    @GetMapping("/log4j2")
    public String log4j2() {
        // xml 配置大小写：Logback 日志小写标签名，Log4j2 日志大写标签名
        // xml 配置大小写：Log4j2 的配置标签支持大小写混合，但官方推荐使用全大写

        // Logback VS Log4j2：Logback 配置是否比 Log4j2 更简单，但功能上 Log4j2 更强大
        // log4j2 VS log4j：log4j2 比 log4j 日志明显提升：支持异步日志，性能更强，支持运行时修改日志级别或配置，无需重启应用等

        // 快速区别 log4j2 and log4j：<Configuration> 和 <log4j:configuration>

        String str = "由于 logback 和 log4j2 的日志配置会有冲突，spring 官方推崇 logback，这里也只能以 logback 日志配置为主\n" +
                "\n" +
                "            springboot 使用配置 log4j2 步骤：\n" +
                "            1.在 pom.xml 中排除 Spring Boot 默认的 Logback 依赖，并引入 Log4j2 的依赖\n" +
                "                exclusion：spring-boot-starter->spring-boot-starter-logging\n" +
                "                include: spring-boot-starter-log4j2\n" +
                "\n" +
                "                <!-- 排除默认的 Logback -->\n" +
                "                <dependency>\n" +
                "                    <groupId>org.springframework.boot</groupId>\n" +
                "                    <artifactId>spring-boot-starter</artifactId>\n" +
                "                    <exclusions>\n" +
                "                        <exclusion>\n" +
                "                            <groupId>org.springframework.boot</groupId>\n" +
                "                            <artifactId>spring-boot-starter-logging</artifactId>\n" +
                "                        </exclusion>\n" +
                "                    </exclusions>\n" +
                "                </dependency>\n" +
                "                <!-- 引入 Log4j2 -->\n" +
                "                <dependency>\n" +
                "                    <groupId>org.springframework.boot</groupId>\n" +
                "                    <artifactId>spring-boot-starter-log4j2</artifactId>\n" +
                "                </dependency>\n" +
                "\n" +
                "            2.application.yml 中显示指定 log4j2 xml 的路径，你可以不指定只要 log4j2 xml 文件名字起的规范就能自动识别\n" +
                "                logging:\n" +
                "                    config: classpath:your-custom-log4j2.xml\n" +
                "\n" +
                "            3.类似的 src/main/resources 创建 log4j2-spring.xml/log4j2.xml，Spring Boot 会自动加载该文件\n" +
                "\n" +
                "            具体配置参考 src/main/resources 下的 log4j2 的配置文件，实际该项目中只能使用一种日志配置即 logback 日志";

        System.out.println(str);
        return "success";
    }

    // 不同日志级别的输出使用
    @GetMapping("/log-use")
    public String logUse() {
        String name = "liming";
        int age = 12;
        Student student = new Student() {{
            setStudentId(1L);
            setSchoolClassId(2L);
        }};
        Exception e = new Exception("一个异常");

        // 不同级别日志，日志级别：TRACE < DEBUG < INFO < WARN < ERROR < FATAL
        log.error("error - name: {}, age: {}, student: {}", name, age, JSON.toJSONString(student), e);
        log.warn("warn - name: {}", name);
        log.info("info - name: {}", name);
        log.debug("debug - name: {}", name);
        log.trace("trace - name: {}", name);

        // 输出对象时候，直接 {} 承接，对象在服务器中打印出来，会呈现 json 格式美化后的效果，但是整体可能会上下很长
        log.info("error - name: {}, age: {}, student: {}", name, age, student);

        return "success";
    }

    // 不同方式使用日志
    @GetMapping("/log-gen")
    public String logGen() {
        // logger 工厂模式生成，或者 @Slf4j 直接使用 log
        log.info("Controller 顶部直接使用 @Slf4j 即可");
        logger.info("或者类中直接声明 private final static Logger logger = LoggerFactory.getLogger(LogController.class);");
        return "success";
    }
}
