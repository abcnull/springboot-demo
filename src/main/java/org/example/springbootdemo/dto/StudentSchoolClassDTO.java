package org.example.springbootdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentSchoolClassDTO {
    /* student 表 */
    private Long studentId;
    private String name;

    /* school_class 表 */
    private Integer classNum;
}
