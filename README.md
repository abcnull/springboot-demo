[toc]

# todo
- 配置 mysql
- 配置 mybatis 或者 plus

# 前置
这里只做框架如何使用的介绍，以及演示 demo，尽量不做其他与框架不相关内容的介绍，比如 sql 语句怎么写等等

# 项目结构

# Springboot 基本使用 demo
## controller 初始化执行
`InitDataController` 展示可以初始化执行一批逻辑
- `onBeanInit()` 最先执行，通过 `@PostConstruct` 标记对应的方法能在某个 bean 的初始化后再执行
- `beanDeclareInit()` 第二执行，通过 `@Bean` 声明来控制方法中要执行的初始化逻辑
- `listenSpecialEvent()` 第三执行，通过实现某个特定事件的监听器，来监听特定的事件，来初始化
- `listenEvent()` 第三执行，通过 @EventListener 来监听标志性的 bean 就绪且应用完全启动的事件，来初始化
- `implementsCommandLineRunner()` 第四执行，通过实现 CommandLineRunner 中的 run 方法来实现初始化，可以访问命令行参数 args
- `implementsApplicationRunner()` 第四执行，通过实现 ApplicationRunner 中的 run 方法来实现初始化，可以访问命令行参数 args，参数更丰富

如果有多个实现 CommandLineRunner 的类，想要让他们按照一定顺序执行可以用 `@Order` 注解
```java
@Component  
@Order(1)  
public class FirstCommandLineRunner implements CommandLineRunner {  
    @Override  
    public void run(String... args) throws Exception {  
        System.out.println("First CommandLineRunner");  
    }  
}
@Component  
@Order(2)  
public class SecondCommandLineRunner implements CommandLineRunner {  
    @Override  
    public void run(String... args) throws Exception {  
        System.out.println("Second CommandLineRunner");  
    }  
}
```
你甚至可以写在一个类中的不同方法中
```java
@Configuration
public class CommandLineRunnerConfig {
    @Bean
    @Order(1)
    public CommandLineRunner firstInitializationRunner() {
        return args -> {
            System.out.println("First initialization task executed.");
            // 执行第一个初始化任务
        };
    }
    @Bean
    @Order(2)
    public CommandLineRunner secondInitializationRunner() {
        return args -> {
            System.out.println("Second initialization task executed.");
            // 执行第二个初始化任务
        };
    }
}
```
`@Order` 用在其他初始化的类是类似用法

一般建议选择 ApplicationRunner 或者监听 ApplicationReadyEvent 的方式，保证在 bean 都被初始化且应用完全启动后执行初始化逻辑
## controller 承接入参数据
### controller 不同的入参数据类型
`BindRequestDataController` controller 接收各种入参类型举例

这些绑定入参的注解的能力算是 SpringMVC 能力的一部分

不同的入参类型举例：
- `reqParams()` 路径入参使用 `@PathVariable`
- `reqParams2()` queryParams 入参使用 `@RequestParam`
- `reqParams3()` json 入参使用 `@RequestBody`
- `reqParams4()` 表单入参使用 `@RequestParam` 映射单个字段
- `reqParams5()` 表单入参使用 `@ModelAttribute` 映射 pojo
- `reqParams6()` 请求头入参使用 `@RequestHeader`
- `reqParams7()` cookie 入参数使用 `@CookieValue`
- `reqParams9()` 文件类型入参使用 `@RequestParam("file") MultipartFile file`
- `reqParams10()` 文件类型入参使用 `@RequestPart("file") MultipartFile file`
- `reqParams11()` 从 URL 路径的分号分隔部分提取键值对，如接口 /path/123;color=red，来提取 `@MatrixVariable(name = "color") String color`
- `reqParams12()` 从请求域中获取属性值（通常由拦截器或过滤器设置），`@RequestAttribute("student") Student student`
- `reqParams13()` 从 Session 中获取属性值，`@SessionAttribute("loginStudent") Student student`

你也可以不使用这些入参的注解，你可以使用其他的比如 HttpServletRequest，通过 `request.getParameter` 来获取入参。
上方其实最常见的还是 `@PathVariable/@RequestBody/@RequestParam/@RequestHeader` 这些

### controller 入参的各种校验方式
`RequestDataValidateController` controller 的入参进行各种校验

校验注解如 `@NotBlank` 属于 java bean validation（JSR 380）规范 
这些校验入参的注解的能力是 SpringMVC 能力的一部分

入参的不同校验方式
- `validUser()` 典型的校验入参对象中的各个 field
  - 先通过各种校验的注解 `@NotBlank/@Email/@Min/@Pattern/@Null/@NotNull` 标记到入参对象中
  - 然后入参对象前通过标记 `@Valid/@@Validated` 来实现校验
  - 校验失败抛出 `MethodArgumentNotValidException`
- `validAndHandle()` 当校验不通过时候执行一些逻辑 
  - 同上，通过 `@Valid` 校验后，你想要执行一些逻辑，那么入参紧跟一个额外参数
   ```java
   @RequestBody @Valid UserDTO userDTO, BindingResult result
   ```
  - 然后通过判定 `hasErrors()` 来执行校验不通过再去执行的逻辑
   ```java
   if (result.hasErrors()) {
   }
   ```
- `validParams()` 可以直接在 controller 方法中校验某个非对象的参数
  - 先在 controller 类上加 `@Validated`
  - 然后 controller 非对象的入参前可以加上诸如 `@Min` 的校验注解
- `validGroup1()` 分组校验，比如某个入参对象中某个 field 可以分成两组校验规则，然后再在 controller 的不同方法中选择用哪个分组的校验规则
  - 入参对象的 field 上定义不同的分组校验
   ```java
   @Null(groups = ValidGroup1.class)  // 创建时id必须为空
   @NotNull(groups = ValidGroup2.class) // 更新时id不能为空
   private Long id;
   ```
  - 再在 controller 的方法中指定使用哪个分组的校验
   ```java
   @RequestBody @Validated(ValidGroup1.class) UserDTO userDTO
   ```
  
平常 `validUser()` 此方法中演示的示例可能更常见

## mybatis 使用
### mybatis 增删改查基本使用
`BasisMybatisController` mybatis 的增删改查基本使用
首先有 Student 这个 model 和数据库的 student 表相映射
然后有 StudentMapper 接口，其中定义了各种对数据库操作的方法
resources 下的 mapper 文件中的 xml 和 StudentMapper interface 对应，其中的方法和 id 也需要对应上，xml 中存在 Mapper 接口方法的具体实现，通过编写 sql 形式
通过 service 类中的方法来调用 mapper 方法来操作数据库，service 中处理业务逻辑
通过 controller 类中的方法来调用 service 方法来执行不同的业务操作
最终实现 controller -> service -> mapper -> mapper.xml

`BasisMybatisController` 其中：
`searchStudentById()` 基本的查询操作，依据 student_id 进行查询
`addStudent()` 基本的插入操作，传入 json 格式的 Student
`updateStudent()` 基本的更新操作，传入 json 格式的 Student，其中 student_id 是必须的，因为依据这个主键来查找
`softDelStudent()` 软删除学生信息，依据传入的 student_id 主键，本质是 update is_deleted 字段
`solidDelStudent()` 硬删除学生信息，依据传入的 student_id 主键，真实删除该学生信息

### mybatis 分页查询
`BasisMybatisPageController` mybatis 的分页查询操作
mybatis 进行分页查询通常有 3 种方式：
1. 使用 pagehelper 分页插件
   - `searchStudentByNamePage` 常规的 pagehelper 分页写法
   - `searchStudentByNamePage2` pagehelper 中使用了 lambda 表达式写法
2. 使用 limit 语句在 xml 自己编写
   - `searchStudentByNamePage3` 使用 limit 语句
3. 在内存中自己做分页
   - `searchStudentByNamePage4` 在内存中进行分页

一般使用 pagehelper 插件来进行分页即可，一般分 3 步骤
1. 引入 pagehelper 依赖，注意和 springboot 的版本对应关系
2. application.yml 中配置 pagehelper 分页参数
3. service 代码中通过 pageHelper 的 `startPage()` 来启动分页，后续需要紧紧跟着分页查询的操作才行；后续通过封装 `PageInfo<T>` 能获取分页的各种信息

### mybatis 联表查询
`MybatisJoinController` 可进行联表查询
多种常见的联表查询操作：
- `searchStudentAndSchoolClassByName()` left join
- `searchStudentAndSchoolClassByName2()` full join
- `searchStudentAndSchoolClassByName3()` inner join
- `searchStudentAndSchoolClassByName4()` 使用表 =，等价于 inner join
- `searchStudentAndSchoolClassByName5()` 代码逻辑上的表连接查询
需要注意的是，对于查询操作的 mapper 来说：
- 如果联合查询的业务逻辑更偏向于表一，应将方法放在表一的 Mapper 中
- 如果联合查询涉及多个业务模块，建议创建一个独立的 Mapper
- 如果联合查询的业务逻辑更偏向于表二，可以放在表二的 Mapper 中，但需谨慎评估业务归属
整体操作是：
- controller，service 写好业务逻辑
- 如果更偏向于表一，表一的 mapper 中声明好联表查询的 mapper 接口
- mapper 对应 xml 中写上联表操作 sql，并且映射到联合查询两表数据的一个 DTO 对象中，因此注意写 resultMap 映射
