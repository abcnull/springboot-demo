package org.example.springbootdemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 学校班级表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolClass {
    // 主键
    private Long schoolClassId;
    // 班级
    private Integer classNum;
    // 年级
    private Integer gradeNum;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Boolean isDeleted;
}
