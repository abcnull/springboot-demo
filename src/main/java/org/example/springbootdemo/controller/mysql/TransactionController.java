package org.example.springbootdemo.controller.mysql;

import org.example.springbootdemo.model.Student;
import org.example.springbootdemo.service.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * mysql+mybatis+springboot 使用事务操作
 */
@RestController
@RequestMapping("/mysql-transaction")
public class TransactionController {
    @Autowired
    private ITransactionService transactionService;

    /**
     * 基本事务操作
     * 使用 @Transactional 表示该方法使用事务，数据源是默认的 mysql 主数据源。
     * 当方法中抛出未捕获的异常时触发回滚，如果是 IOException/SQLException 等某些异常不会处罚回滚。
     * 如果你在方法中 try catch 了这个异常，并且 catch 后不再往外抛出，则不会触发回滚，除非继续往外抛出，
     * 发生回滚的不仅仅是 try catch 中的，而是整个方法内事务对应的数据源的数据库操作都会回滚
     */
    @GetMapping("/basic-transaction")
    public String basicTransaction() {
        Student student = new Student();
        student.setName("zhanghua");
        student.setAge(12);
        transactionService.basicTransactionFunc(student);
        return "success";
    }

    /**
     * 如果你不希望通过 @Transactional 注解的方式，或者希望当满足某一个条件时候(不一定是抛异常)，才手动提交事务，
     * 那么你需要自己去操作 DateSourceTransactionManager 了，
     * 如果 application.yml 常规配置好了 mysql 和 mybatis 那么 service 中直接通过 @Autowire 可以直接引用 DateSourceTransactionManager 了
     * 操作 DateSourceTransactionManager 可以手动来提交事务，等价于方法上使用 @Transactional 注解
     */
    @GetMapping("/transaction-manual")
    public String manualTransaction() {
        Student student = new Student();
        student.setName("zhanghua");
        student.setAge(12);
        transactionService.transactionByYourself(student);
        return "success";
    }

//    /**
//     * 当 application.yml 要配置多个 mysql 数据源，对于方法的运行你启用事务，你想指定哪个数据源的事务进行回滚时
//     * 可以在 @Transactional 注解中指定事务管理器的 bean name
//     */
//    @GetMapping("/muti-datasource-transaction")
//    public String mutiDataSourceTransaction() {
//        Student student = new Student();
//        student.setName("zhanghua");
//        student.setAge(12);
//        transactionService.mutiDataSourceTransaction(student);
//        return "success";
//    }
}
