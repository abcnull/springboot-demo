package org.example.springbootdemo.service;


import org.example.springbootdemo.model.Student;

import java.util.Optional;

/**
 * 基础的 mybatis 的增删改查操作
 */
public interface IBasisMybatisService {
    Optional<Student> searchStudentById(Long studentId);
    boolean addStudent(Student student);
    boolean updateStudent(Long studentId, Student student);
    boolean softDelStudent(Long studentId);
    boolean solidDelStudent(Long studentId);
}
