package org.example.springbootdemo.controller.mybatis;

import org.example.springbootdemo.dto.StudentSchoolClassDTO;
import org.example.springbootdemo.service.IMybatisJoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * mybatis 联表查询
 */
@RestController
@RequestMapping("/mybatis-join")
public class MybatisJoinController {
    @Autowired
    private IMybatisJoinService mybatisJoinService;

    // left join
    @GetMapping("/left-join")
    public List<StudentSchoolClassDTO> searchStudentAndSchoolClassByName(@RequestParam String name) {
        return mybatisJoinService.selectLeftJoin(name);
    }

    // full join
    @GetMapping("/full-join")
    public List<StudentSchoolClassDTO> searchStudentAndSchoolClassByName2(@RequestParam String name) {
        return mybatisJoinService.selectFullJoin(name);
    }

    // inner join
    @GetMapping("/inner-join")
    public List<StudentSchoolClassDTO> searchStudentAndSchoolClassByName3(@RequestParam String name) {
        return mybatisJoinService.selectInnerJoin(name);
    }

    // where 相等，等价于 inner join
    @GetMapping("/where-equal")
    public List<StudentSchoolClassDTO> searchStudentAndSchoolClassByName4(@RequestParam String name) {
        return mybatisJoinService.selectJoin(name);
    }

    // 代码逻辑上做联表查询
    @GetMapping("/by-code")
    public List<StudentSchoolClassDTO> searchStudentAndSchoolClassByName5(@RequestParam String name) {
        return mybatisJoinService.selectJoin2(name);
    }
}
