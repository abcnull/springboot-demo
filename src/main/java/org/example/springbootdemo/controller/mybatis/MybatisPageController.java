package org.example.springbootdemo.controller.mybatis;

import org.example.springbootdemo.model.Student;
import org.example.springbootdemo.service.IMybatisPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * mybatis 分页查询操作 todo done
 */
@RestController
@RequestMapping("/mybatis-page")
public class MybatisPageController {
    @Autowired
    private IMybatisPageService basisMybatisPageService;

    // 使用 pagehelper 分页插件分页
    @GetMapping("/search-page")
    public List<Student> searchStudentByNamePage(@RequestParam String name, @RequestParam int pageNum, @RequestParam int pageSize) {
        return basisMybatisPageService.searchStudentByPage(name, pageNum, pageSize);
    }

    // 使用 pagehelper 分页插件分页
    // 使用了 pagehelper 分页的 lambda 表达式写法
    @GetMapping("/search-page2")
    public List<Student> searchStudentByNamePage2(@RequestParam String name, @RequestParam int pageNum, @RequestParam int pageSize) {
        return basisMybatisPageService.searchStudentByPage2(name, pageNum, pageSize);
    }

    // 手动在 xml 中使用 limit 来进行分页
    @GetMapping("/search-page3")
    public List<Student> searchStudentByNamePage3(@RequestParam String name, @RequestParam int pageNum, @RequestParam int pageSize) {
        return basisMybatisPageService.searchStudentByPage3(name, pageNum, pageSize);
    }

    // 直接在内存中写逻辑来分页
    @GetMapping("/search-page4")
    public List<Student> searchStudentByNamePage4(@RequestParam String name, @RequestParam int pageNum, @RequestParam int pageSize) {
        return basisMybatisPageService.searchStudentByPage4(name, pageNum, pageSize);
    }
}
