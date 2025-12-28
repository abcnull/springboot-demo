package org.example.springbootdemo.controller.respdata;

import org.example.springbootdemo.common.ApiResponse;
import org.example.springbootdemo.mapper.StudentMapper;
import org.example.springbootdemo.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 多种不同方式和类型的 controller 层返回数据
 */
@RestController
@RequestMapping("/muti-response-data")
public class MutiResponseDataController {
    @Autowired
    public StudentMapper studentMapper;

    // 返回 String
    // 简单直接的返回文本
    @GetMapping("/str")
    public String strResp() {
        return "hello, world!";
    }

    // 返回 springboot 自己 ResponseEntity<T> 封装
    // 不常用
    // 支持链式调用（如 ResponseEntity.ok().header("X-Custom-Header", "value").body(data)），灵活设置 HTTP 状态码（如 200、404、500）
    // ResponseEntity 方式其实对象其中也不含有业务状态码和业务错误信息，它较依赖于 http 状态码去做判断，这一点与国内开发常常依赖返回数据中的业务状态码和业务错误信息不同，因此不太常用
    @GetMapping("/ResponseEntity")
    public ResponseEntity<Student> responseEntityResp() {
        Student student = studentMapper.selectStudentById(1L);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    // 返回对象 json，使用 @ResponseBody 注解
    // 不常用
    // @RestController = @Controller + @ResponseBody，其实 controller 上已经是 @RestController，方法上没有必要再写 @ResponseBody，也能达到一样的效果
    // 适合简单数据返回，因为其中其实一般没有含有状态码和错误信息
    @GetMapping("/annotation")
    @ResponseBody
    public Student annotationResp() {
        return studentMapper.selectStudentById(1L);
    }

    // 返回自定义对象 json，统一相应格式封装
    // 常用，前后端对齐
    @GetMapping("/custom")
    public ApiResponse<Student> customResp() {
        Student student = studentMapper.selectStudentById(1L);
        return ApiResponse.success(student);
    }
}
