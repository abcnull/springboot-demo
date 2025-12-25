package org.example.springbootdemo.service.impl;

import org.example.springbootdemo.annotation.LogOperation;
import org.example.springbootdemo.service.IAopService;
import org.springframework.stereotype.Service;

/**
 * aop Service
 */
@Service
public class AopServiceImpl implements IAopService {
    // @LogOperation 注解在方法这里进行了切面，起到打印日志
    // 注意 OperationLogAspect 这个关键类，写了切面具体的操作逻辑
    // 根据名字查询学生
    @LogOperation(value = "依据姓名查询学生", operationType = "search", saveRequestParams = true)
    @Override
    public String searchStudent(String name) {
        // 模拟查询数据库
        return "张三";
    }
}
