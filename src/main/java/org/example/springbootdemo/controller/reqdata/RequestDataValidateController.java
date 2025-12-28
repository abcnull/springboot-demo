package org.example.springbootdemo.controller.reqdata;

import org.example.springbootdemo.dto.UserDTO;
import org.example.springbootdemo.dto.ValidGroup1;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * 各种常用的入参校验注解使用
 */
@RestController
@RequestMapping("/req-data-validate")
@Validated
public class RequestDataValidateController {

    // @Validated/@Valid 校验对象
    // 首先 dto 对象中通过一些校验注解标记
    // @Validated/@Valid 校验失败抛出 MethodArgumentNotValidException，需全局异常处理
    @PostMapping("/valid-obj")
    public String validUser(@RequestBody @Valid UserDTO userDTO) {
        System.out.println("校验对象参数");
        return "success";
    }

    // 手动处理校验失败后要做什么业务逻辑
    // BindingResult 紧紧跟着 @Valid 参数的后头
    @GetMapping("/valid-and-handle")
    public String validAndHandle(@RequestBody @Valid UserDTO userDTO, BindingResult result) {
        if (result.hasErrors()) {
            System.out.println("如果校验有问题，这里执行校验有问题时候此时的业务逻辑");
            return "fail";
        }
        // 正常业务逻辑执行
        System.out.println("正常业务执行");
        return "success";
    }

    // 首先直接在 controller 类上写上 @Validated 注解
    // 然后直接在参数写上校验的注解
    @GetMapping("/valid-params/{id}")
    public String validParams(@PathVariable @Min(1) Long id, @RequestParam @NotBlank String name) {
        System.out.println("类上加上 @Validated 注解，方法入参加上校验的注解，如 @Min(1)");
        return "success";
    }

    // 分组校验
    // 使用 ValidGroup1 分组对 UserDTO 中的参数做校验
    @PostMapping("/valid-group1")
    public String validGroup1(@RequestBody @Validated(ValidGroup1.class) UserDTO userDTO) {
        System.out.println("分组校验，使用 UserDTO 中的分组 1 来对其参数做校验");
        return "success";
    }
}
