package org.example.springbootdemo.annotation;

import java.lang.annotation.*;

/**
 * 自定义 log 日志的注解
 */
@Target(ElementType.METHOD) // 方法上的注解
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogOperation {
    /**
     * 操作描述
     */
    String value() default "";

    /**
     * 操作类型
     */
    String operationType() default "OPERATE";

    /**
     * 是否保存请求参数
     */
    boolean saveRequestParams() default true;
}
