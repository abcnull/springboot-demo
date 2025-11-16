package org.example.springbootdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Email(message = "邮箱格式错误")
    private String email;

    @Min(value = 18, message = "年龄需≥18岁")
    private Integer age;

    @Pattern(regexp = "1[3-9]\\d{9}", message = "手机号格式错误")
    private String phone;

    // 分组校验规则
    @Null(groups = ValidGroup1.class)  // 创建时id必须为空
    @NotNull(groups = ValidGroup2.class) // 更新时id不能为空
    private Long id;
}
