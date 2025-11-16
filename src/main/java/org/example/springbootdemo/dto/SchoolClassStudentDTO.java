package org.example.springbootdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.springbootdemo.model.Student;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolClassStudentDTO {
    private Long schoolClassId;
    private Integer gradeNum;
    private Integer classNum;
    private List<Student> studentList;
}
