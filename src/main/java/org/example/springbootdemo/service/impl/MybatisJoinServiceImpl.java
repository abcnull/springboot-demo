package org.example.springbootdemo.service.impl;

import org.example.springbootdemo.dto.StudentSchoolClassDTO;
import org.example.springbootdemo.mapper.SchoolClassMapper;
import org.example.springbootdemo.mapper.StudentMapper;
import org.example.springbootdemo.model.SchoolClass;
import org.example.springbootdemo.model.Student;
import org.example.springbootdemo.service.IMybatisJoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MybatisJoinServiceImpl implements IMybatisJoinService {
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private SchoolClassMapper schoolClassMapper;

    // left join
    @Override
    public List<StudentSchoolClassDTO> selectLeftJoin(String name) {
        return studentMapper.selectStudentSchoolClass(name);
    }

    // full join
    @Override
    public List<StudentSchoolClassDTO> selectFullJoin(String name) {
        return studentMapper.selectStudentSchoolClass2(name);
    }

    // inner join
    @Override
    public List<StudentSchoolClassDTO> selectInnerJoin(String name) {
        return studentMapper.selectStudentSchoolClass3(name);
    }

    // where =
    @Override
    public List<StudentSchoolClassDTO> selectJoin(String name) {
        return studentMapper.selectStudentSchoolClass4(name);
    }

    // 代码逻辑执行 2 表查询
    @Override
    public List<StudentSchoolClassDTO> selectJoin2(String name) {
        // 依据 name 查询 student
        List<Student> students = studentMapper.selectStudentByName(name);

        // 转换
        List<Long> schoolClassIds = students.stream().map(Student::getSchoolClassId).collect(Collectors.toList());

        // 依据 school_class_id 查 school_class
        List<SchoolClass> schoolClasses = schoolClassMapper.selectSchoolClassByIds(schoolClassIds);

        // 聚合 StudentSchoolClassDTO，实现 left join
        List<StudentSchoolClassDTO> studentSchoolClassDTOs = new ArrayList<>();
        students.forEach(student -> {
            schoolClasses.forEach(schoolClass -> {
                // 联表关键
                if (Objects.equals(student.getSchoolClassId(), schoolClass.getSchoolClassId())) {
                    StudentSchoolClassDTO studentSchoolClassDTO = new StudentSchoolClassDTO();
                    studentSchoolClassDTO.setStudentId(student.getStudentId());
                    studentSchoolClassDTO.setName(student.getName());
                    studentSchoolClassDTO.setClassNum(schoolClass.getClassNum());
                    // add
                    studentSchoolClassDTOs.add(studentSchoolClassDTO);
                }
            });
        });
        return studentSchoolClassDTOs;
    }
}
