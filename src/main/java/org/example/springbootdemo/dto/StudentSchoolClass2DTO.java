package org.example.springbootdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.springbootdemo.model.SchoolClass;
import org.example.springbootdemo.model.Student;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentSchoolClass2DTO {
    /* student 表 */
    private Long studentId;
    private String name;

    /* school_class 表 */
    private SchoolClass schoolClass;
}
