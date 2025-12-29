package org.example.springbootdemo.controller.reqdata;

import com.alibaba.fastjson.JSON;
import org.example.springbootdemo.model.Student;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * controller 层绑定各种类型的请求数据举例
 */
@RestController
@RequestMapping("/bind-req-data")
public class BindRequestDataController {
    // 路径中的参数
    // @PathVariable("studentId") 这里的 studentId 和路径中占位符一致
    /*
    value：路径变量名（必填）。若方法参数名与路径变量名一致，可省略
    required：是否必需，默认 true
    * */
    @GetMapping("/search/{studentId}")
    public String reqParams(@PathVariable String studentId) {
        System.out.println("路径中的参数: " + studentId);
        return "success";
    }

    // queryParams 参数
    /*
    value：参数名（必填）。若方法参数名与请求参数名一致，可省略
    required：是否必需，默认 true。若为 false，参数可缺省
    defaultValue：默认值。若设置，默认值会覆盖 required 属性（即使未传参数也返回默认值）
    * */
    @GetMapping("/search")
    public String reqParams2(@RequestParam String name) {
        System.out.println("queryParams 参数: " + name);
        return name;
    }

    // json 请求体参数
    // 前端 json 中 key 往往是下划线，后端该实体的 field 往往又是小驼峰，这样会导致 @RequestBody 默认是映射不过来的
    @PostMapping("/insert-json")
    public String reqParams3(@RequestBody Student student) {
        System.out.println("json 参数: " + JSON.toJSONString(student));
        return "success";
    }

    // form 表单请求参数
    // 此种方式是通过 @RequestParam 映射
    @PostMapping("/insert-form")
    public String reqParams4(@RequestParam("username") String username, @RequestParam("password") String password) {
        System.out.println("form 表单参数: " + username + " 和 " + password);
        return "success";
    }

    // form 表单请求参数
    // 此种方式是通过 @ModelAttribute 映射 pojo
    @PostMapping("/insert-form2")
    public String reqParams5(@ModelAttribute Student student) {
        System.out.println("form 表单参数: " + JSON.toJSONString(student));
        return "success";
    }

    // 获取请求头 key 对应的 value
    // 常见获取请求头 Authorization 的值
    /*
    value：请求头字段名（必填）
    required：是否必需，默认 true
    defaultValue：默认值
    * */
    @GetMapping("/verify")
    public String reqParams6(@RequestHeader("Authorization") String token) {
        System.out.println("请求头 Authorization 参数: " + token);
        return "success";
    }

    // 获取 cookie 对应的 value
    /*
    value：Cookie 名称（必填）
    required：是否必需，默认 true
    defaultValue：默认值
    * */
    @GetMapping("/cookie")
    public String reqParams7(@CookieValue("JSESSIONID") String sessionId) {
        System.out.println("请求头 Cookie 参数: " + sessionId);
        return "success";
    }

    // 使用原生 HttpServletRequest 获取其中的参数
    @GetMapping("/raw")
    public String reqParams8(HttpServletRequest request) {
        String name = request.getParameter("name");
        System.out.println("HttpServletRequest 提取参数: " + name);
        return "success";
    }

    // 文件上传
    // 使用 @RequestParam 映射，使用 MultipartFile 接口承接
    @GetMapping("/file")
    public String reqParams9(@RequestParam("file") MultipartFile file) {
        System.out.println("file 文件上传提取: " + file.getName());
        return "success";
    }

    // 文件上传
    // 处理 multipart/form-data 请求中的文件或数据部分，文件上传或混合数据（文件 + 表单字段）提交
    /*
    value：请求部分的名称（必填）
    required：是否必需，默认 true
    * */
    @GetMapping("/file2")
    public String reqParams10(@RequestPart("file") MultipartFile file, @RequestPart("description") String description) {
        System.out.println("file 文件上传提取: " + file.getName() + " 和 " + description);
        return "success";
    }

    // 从 URL 路径的分号分隔部分提取键值对
    // 如接口 /path/123;color=red
    /*
    value：变量名（必填）
    required：是否必需，默认 true
    pathVar：关联的路径变量名（如 /path/{id};color=red 中的 id）
    * */
    @GetMapping("/path/{id}")
    public String reqParams11(@PathVariable("id") String id, @MatrixVariable(name = "color") String color) {
        System.out.println("path 中提取的参数: " + color);
        return "success";
    }

    // 从请求域中获取属性值（通常由拦截器或过滤器设置）
    // 适用：请求转发时共享数据
    /*
    value：属性名（必填）
    required：是否必需，默认 true
    * */
    @GetMapping("/forward")
    public String reqParams12(@RequestAttribute("student") Student student) {
        System.out.println("请求域获取参数: " + JSON.toJSONString(student));
        return "success";
    }

    // 从 Session 中获取属性值
    // 适用场景：跨请求共享数据（如登录用户信息）
    /*
    value：Session 属性名（必填）
    required：是否必需，默认 true
    * */
    @GetMapping("/session")
    public String reqParams13(@SessionAttribute("loginStudent") Student student) {
        System.out.println("session 获取参数: " + JSON.toJSONString(student));
        return "success";
    }
}
