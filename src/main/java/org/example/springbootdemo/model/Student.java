package org.example.springbootdemo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 学生表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    // 学号
    @JsonProperty("student_id")
    private Long studentId;
    private String name;
    private Integer age;
    // 对应的班级
    @JsonProperty("school_class_id")
    private Long schoolClassId;
    // 创建时间
    /*
    数据库对应的是
    * */
    private LocalDateTime createTime;
    // 更新时间
    private LocalDateTime updateTime;
    @JsonProperty("is_deleted")
    private Boolean isDeleted;

    public void initName() {
        this.name = "xiaoming";
    }
}
