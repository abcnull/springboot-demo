package org.example.springbootdemo.service;

import org.example.springbootdemo.model.Student;

/**
 * mysql+mybatis+springboot 事务操作 Iservice
 */
public interface ITransactionService {
    void basicTransactionFunc(Student student);

    void transactionByYourself(Student student);

//    void mutiDataSourceTransaction(Student student);
}
