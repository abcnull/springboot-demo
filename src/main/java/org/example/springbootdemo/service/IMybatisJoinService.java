package org.example.springbootdemo.service;

import org.example.springbootdemo.dto.StudentSchoolClassDTO;

import java.util.List;

/**
 * mybatis 联表查询
 */
public interface IMybatisJoinService {
    List<StudentSchoolClassDTO> selectLeftJoin(String name);

    List<StudentSchoolClassDTO> selectFullJoin(String name);

    List<StudentSchoolClassDTO> selectInnerJoin(String name);

    List<StudentSchoolClassDTO> selectJoin(String name);

    List<StudentSchoolClassDTO> selectJoin2(String name);
}
