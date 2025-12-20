package org.example.springbootdemo.controller.mybatis;

import org.example.springbootdemo.model.Student;
import org.example.springbootdemo.service.IBasisMybatisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * 基础的 mybatis 的增删改查操作
 */
@RestController
@RequestMapping("/basis-mybatis")
public class BasisMybatisController {
    @Autowired
    private IBasisMybatisService basisMybatisService;

    // 依据 studentId 查询学生信息
    @GetMapping("/search/{studentId}")
    public Optional<Student> searchStudentById(@PathVariable String studentId) {
        // todo 返回值待修改
        return basisMybatisService.searchStudentById(Long.valueOf(studentId));
    }

    // 新增学生信息
    @PostMapping("/add")
    public boolean addStudent(@RequestBody Student student) {
        return basisMybatisService.addStudent(student);
    }

    // 依据 studentId 更新学生信息
    @PostMapping("/update")
    public boolean updateStudent(@RequestBody Student student) {
        return basisMybatisService.updateStudent(student.getStudentId(), student);
    }

    // 依据 studentId 软删除学生信息
    @PostMapping("/soft-delete")
    public boolean softDelStudent(@RequestBody Long studentId) {
        return basisMybatisService.softDelStudent(studentId);
    }

    // 依据 studentId 硬删除学生信息
    @PostMapping("/solid-delete")
    public boolean solidDelStudent(@RequestBody Long studentId) {
        return basisMybatisService.solidDelStudent(studentId);
    }
}
