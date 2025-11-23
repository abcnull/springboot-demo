package org.example.springbootdemo.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.springbootdemo.mapper.StudentMapper;
import org.example.springbootdemo.model.Student;
import org.example.springbootdemo.service.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * mysql+mybatis+springboot 事务操作 service
 */
@Slf4j
@Service
public class TransactionServiceImpl implements ITransactionService {
    @Autowired
    private DataSourceTransactionManager dataSourceTransactionManager;

    @Autowired
    private StudentMapper studentMapper;

    /**
     * 基本事务操作
     * 使用 @Transactional 表示该方法使用事务，数据源是默认的 mysql 主数据源。
     * 当方法中抛出未捕获的异常时触发回滚，如果是 IOException/SQLException 等某些异常不会处罚回滚。
     * 如果你在方法中 try catch 了这个异常，并且 catch 后不再往外抛出，则不会触发回滚，除非继续往外抛出，
     * 发生回滚的不仅仅是 try catch 中的，而是整个方法内事务对应的数据源的数据库操作都会回滚
     *
     * @param student student
     */
    @Transactional
    @Override
    public void basicTransactionFunc(Student student) {
        try {
            studentMapper.insertStudent(student); // insert student
        } catch (Exception e) {
            log.info("basicTransactionFunc, 若 insert 抛出异常，catch 住了，整个方法不抛出异常，那么 insert 和 update 动作都不会回滚");
        }
        student.setName("xiaozhang");
        studentMapper.updateStudent(student); // update student
        log.info("basicTransactionFunc, 若 update 抛出异常了，那么整个事务对应的数据源的操作：insert 和 update 操作都会回滚");
    }

    /**
     * 如果你不希望通过 @Transactional 注解的方式，或者希望当满足某一个条件时候(不一定是抛异常)，才手动提交事务，
     * 那么你需要自己去操作 DateSourceTransactionManager 了，
     * 如果 application.yml 常规配置好了 mysql 和 mybatis 那么 service 中直接通过 @Autowire 可以直接引用 DateSourceTransactionManager 了
     * 操作 DateSourceTransactionManager 可以手动来提交事务，等价于方法上使用 @Transactional 注解
     *
     * @param student student
     */
    @Override
    public void transactionByYourself(Student student) {
        // 可以先定义事务等属性
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("MyManualTransaction"); // 事务名称
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        // 获取事务状态
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(def);

        try {
            studentMapper.insertStudent(student); // insert student
            student.setName("xiaozhang");
            studentMapper.updateStudent(student); // update student

            // commit 事务
            dataSourceTransactionManager.commit(transactionStatus);
        } catch (Exception e) {
            log.info("transactionByYourself, 事务失败触发手动回滚");
            // rollback 事务
            dataSourceTransactionManager.rollback(transactionStatus);
            throw e;
        }
    }


//    /**
//     * 当 application.yml 要配置多个 mysql 数据源，对于方法的运行你启用事务，你想指定哪个数据源的事务进行回滚时
//     * 可以在 @Transactional 注解中指定事务管理器的 bean name
//     * 对应 config 文件中的 transaction 的数据源配置
//     *
//     * @param student student
//     */
//    @Transactional(transactionManager = "primaryTransactionManager")
//    @Override
//    public void mutiDataSourceTransaction(Student student) {
//        try {
//            studentMapper.insertStudent(student); // insert student
//        } catch (Exception e) {
//            log.info("mutiDataSourceTransaction, 若 insert 抛出异常，catch 住了，整个方法不抛出异常，那么 insert 和 update 动作都不会回滚");
//        }
//        student.setName("xiaozhang");
//        studentMapper.updateStudent(student); // update student
//        log.info("mutiDataSourceTransaction, 若 update 抛出异常了，那么整个事务对应的数据源的操作：insert 和 update 操作都会回滚");
//    }
}
