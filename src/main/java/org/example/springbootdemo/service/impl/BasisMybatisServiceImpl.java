package org.example.springbootdemo.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.springbootdemo.mapper.StudentMapper;
import org.example.springbootdemo.model.Student;
import org.example.springbootdemo.service.IBasisMybatisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 这里主要做了 mybatis 的基础增删改查操作
 * 1.查询
 * 2.新增
 * 3.更新
 * 4.逻辑删除
 * 5.真实删除
 */
@Slf4j
@Service
public class BasisMybatisServiceImpl implements IBasisMybatisService {
    @Autowired
    public StudentMapper studentMapper;

    /**
     * select
     * 根据学号查询学生
     *
     * @param studentId 学号
     * @return 查询到的学生
     */
    public Optional<Student> searchStudentById(Long studentId) {
        try {
            return Optional.ofNullable(studentMapper.selectStudentById(studentId));
        } catch (Exception e) {
            log.error("searchStudentById,查询学生信息失败,studentId:{}", studentId, e);
            throw new RuntimeException("查询学生信息失败", e);
        }
    }

    /**
     * insert
     * 新增学生
     *
     * @param student 学生数据
     * @return 学生数据
     */
    public boolean addStudent(Student student) {
        try {
            studentMapper.insertStudent(student);
        } catch (Exception e) {
            log.error("addStudent,新增学生失败", e);
            throw new RuntimeException("新增学生失败", e);
        }
        return true;
    }

    /**
     * update
     * 更新学生信息
     *
     * @param studentId 学号
     * @param student   学生数据
     */
    public boolean updateStudent(Long studentId, Student student) {
        try {
            studentMapper.updateStudent(student);
        } catch (Exception e) {
            log.error("updateStudent,更新学生信息失败,studentId:{}", studentId, e);
            throw new RuntimeException("更新学生信息失败", e);
        }
        return true;
    }

    /**
     * delete
     * 根据学号逻辑删除学生数据
     *
     * @param studentId 学号
     * @return 是否删除成功
     */
    public boolean softDelStudent(Long studentId) {
        try {
            studentMapper.updateStudentIsDeletedById(studentId);
        } catch (Exception e) {
            log.error("softDelStudent,软删除学生失败,studentId:{}", studentId, e);
            return false;
        }
        return true;
    }

    /**
     * delete
     * 根据学号真实删除学生数据
     *
     * @param studentId 学号
     * @return 是否删除成功
     */
    public boolean solidDelStudent(Long studentId) {
        try {
            studentMapper.deleteStudentById(studentId);
        } catch (Exception e) {
            log.error("solidDelStudent,硬删除学生失败,studentId:{}", studentId, e);
            return false;
        }
        return true;
    }
}
