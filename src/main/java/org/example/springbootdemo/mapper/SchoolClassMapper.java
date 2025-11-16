package org.example.springbootdemo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.springbootdemo.dto.SchoolClassStudentDTO;
import org.example.springbootdemo.dto.StudentSchoolClassDTO;
import org.example.springbootdemo.model.SchoolClass;

import java.util.List;

@Mapper
public interface SchoolClassMapper {
    // 依据 in school_class_id 查 school_class
    List<SchoolClass> selectSchoolClassByIds(@Param("schoolClassIds") List<Long> schoolClassIds);

    // <collection> 标签在 <resultMap> 中的使用，通常表示 list，即 1 对 n 的关系。比如一个班级对象中可能有一批学生
    // 依据 schoolClassId 查询班级学生 dto 数据
    SchoolClassStudentDTO selectSchoolClassById(@Param("schoolClassId") Long schoolClassId);

    // <association> 标签在 <resultMap> 中的使用，通常表示 1 对 1 的关系。比如一个学生，只对应一个班级
    // 依据 studentId 查询班级学生 dto 数据
    StudentSchoolClassDTO selectStudentById(@Param("studentId") Long studentId);
}
