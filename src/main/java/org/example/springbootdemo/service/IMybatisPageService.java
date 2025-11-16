package org.example.springbootdemo.service;

import org.example.springbootdemo.model.Student;

import java.util.List;

/**
 * mybatis 分页查询
 */
public interface IMybatisPageService {
    List<Student> searchStudentByPage(String name, int pageNum, int pageSize);

    List<Student> searchStudentByPage2(String name, int pageNum, int pageSize);

    List<Student> searchStudentByPage3(String name, int pageNum, int pageSize);

    List<Student> searchStudentByPage4(String name, int pageNum, int pageSize);
}
