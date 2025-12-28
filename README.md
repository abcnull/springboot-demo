# 1.前置
这里只做框架如何使用的介绍，以及演示 springboot demo，以及 springboot 和其他各种中间件的结合的 demo，
尽量不做其他内容的介绍，比如 sql 语句怎么写，java 语法是如何的等等

下方演示的 jdk 1.8 + springboot 2.x demo 从 controller 层起步，往 service 层看代码逻辑，能更清晰看到不同的 demo 的展示

# 2.项目结构
``` txt
- springboot-demo
    - log
    - src
        - main
            - java
                - org.example.springbootdemo
                    - annotation 自定义注解
                    - aspect 自定义切面
                    - common 公用包
                    - config 配置类
                    - constant 常量类
                    - controller 控制器
                    - dto 数据传输对象
                    - esdao  Elasticsearch 数据访问对象
                    - esmodel Elasticsearch 模型类，表对象
                    - init 初始化类
                    - mapper Mybatis 映射类
                    - model 模型类，表对象
                    - mongodbdao MongoDB 数据访问对象
                    - mongodbmodel MongoDB 模型类，表对象
                    - schedule 定时任务类
                    - service 服务类
                        - impl 服务类实现类
                    - util 工具类
                    - SpringbootDemoApplication 应用主类，启动类
            - resources
                - mapper Mybatis 映射 xml
                - application.yml 应用配置文件
                - log4j2-spring.xml 日志配置文件（或者 logback-spring.xml 日志）
        - test
            - java
                - 测试类
            - resources
    - pom.xml
    - README.md
```

# 3.Springboot 基本使用 demo
## 3.1 初始化执行
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
## 3.2 controller 承接入参数据
### 3.2.1 controller 不同的入参数据类型
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

### 3.2.2 controller 入参的各种校验方式
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

## 3.3 过滤器 filter
springboot 中过滤器 `FilterController` 常做一些业务无关的操作：
1. 日志过滤，可覆盖所有请求，实现低侵入的性能评估
2. cors 跨域处理

怎么写：
1. 通过编写一个过滤器，并通过 `@WebFilter` 标注，来实现 implements Filter（注解 `@WebFilter` 为 servlet 提供非 springboot 提供）
2. 配合 springboot 启动类加上 `@ServletComponentScan` 使其生效，但是更常见的做法是再编写一个 `CustomFilterConfig` 类（其标有 `@Configuration`，并且通过 `@Bean` 来注册过滤器使用）,下面示例注册了 `Demo1Filter` 这个过滤器
``` java
@Configuration
public class CustomFilterConfig {
    @Bean
    public FilterRegistrationBean<Demo1Filter> demo1Filter() {
        FilterRegistrationBean<Demo1Filter> registration = new FilterRegistrationBean<>();

        registration.setFilter(new Demo1Filter());
        registration.addUrlPatterns("/*"); // 指定过滤器的 URL 模式
        registration.setOrder(2); // 设置 Filter 的执行顺序 2

        return registration;
    }
}
```
3. 后续请求过来，请求进入 web 容器后，但是进入 servlet 之前先进行预处理，这里经过过滤器过滤，如果过滤器没有把它过滤掉，那么请求后续达到 servlet 进行派发，在达到指定 controller 的接口之前又会被拦截器处理

## 3.4 拦截器 interceptor
springboot 中拦截器 `InterceptorController` 常做一些业务相关的操作：
1. 权限校验，比如判断用户是否登录，是否有操作某个资源的权限
当然也可以做一些其他的业务相关操作，比如记录请求的参数、响应结果、处理时间等信息

怎么写：
1. 编写一个拦截器类 `AuthInterceptor` implements HandlerInterceptor，然后在 `preHandle()` 中编写比如方法执行进行拦截的逻辑
2. 然后 `CustomInterceptorConfig` implements WebMvcConfigurer 中去注册拦截器，还可以指定拦截器拦截的路径，和过滤器比较像
``` java
@Configuration
public class CustomInterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private AuthInterceptor authInterceptor;
    @Autowired
    private LoggingInterceptor loggingInterceptor;

    //将拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 AuthInterceptor
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**") // 拦截所有的 url
                .excludePathPatterns("/user/login") // 排除url: /user/login (登录)
                .excludePathPatterns("/user/reg") // 排除url: /user/reg   (注册)
                .excludePathPatterns("/image/**") // 排除 image(图像) 文件夹下的所有文件
                .excludePathPatterns("/**/*.js") // 排除任意深度目录下的所有".js"文件
                .excludePathPatterns("/**/*.css") // 排除任意深度目录下的所有".css"文件
                .order(1); // 数值越小优先级越高

        // 注册 LoggingInterceptor
        registry.addInterceptor(loggingInterceptor)
                .addPathPatterns("/**")
                .order(2);
    }
}
```

## 3.5 mybatis 使用
### 3.5.1 mybatis 增删改查基本使用
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

### 3.5.2 mybatis 分页查询
`MybatisPageController` mybatis 的分页查询操作
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

### 3.5.3 mybatis 联表查询
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

### 3.5.4 mybatis 接口对应 mapper.xml 文件的各种写法
`MutiSqlXmlController` 中演示 mapper xml 的多种写法

- `<select>`: `/select` 普通查询
- `<insert>`: `/insert` 插入操作
- `<update>`: `/update` 更新操作
- `<delete>`: `/delete` 删除操作
- `<if>`: `/if` 条件判断，根据条件判断 sql 中需要展示什么内容
- `<where>`: `/where` where 标签比如在查询时，能智能的处理 and 或者 or 关键字，并且当 where 标签中真的有条件语句时候，这个 where 关键字才会被灵活的生成否则不生成
- `<set>`: `/set` set 标签会职能的自动处理末尾多余的逗号
- `<choose>/<when>/<otherwise>`: `/choose_when_otherwise` 类似 java 中的 switch case 语句，choose 类似 switch，when 类似 case，otherwise 类似 default。choose 结构天生适合处理互斥条件。如果条件不是互斥的，使用多个 if 可能更合适
- `<foreach>`: `/foreach` 实现遍历，比如 in 某个 list
- `<trim>`: `/trim` 它可以通过添加或移除内容的前缀、后缀，或者替换掉多余的关键字（如 AND、OR、逗号等）
- `<bind>`: `/bind` 用于在 OGNL 表达式上下文中创建一个变量，并将其绑定到当前上下文中，通常用于简化动态 SQL 中的复杂表达式、避免重复计算
- `<parameterType>`: `/parameterType` 一般情况下可以省略不写，mybatis 能自动推断出来类型，但是写上能更直观清晰，推荐写生的比如当入参是 pojo 实体类，parameterType 能自动推断入参的数据类型，比如：
  - 基本数据类型：int，long，boolean 等
  - 包装数据类型：Integer，Long 等
  - String 类型
  - 自定义的 pojo 对象：比如 Student，User 等
  - 集合类型：比如 List，Map 等
- `<resultMap>/<resultType>`: `/resultMap_resultType` resultMap 先定义好 数据库 column 怎么 映射到 对象中的 field 中，resultType 如果是 mybatis plus 使用其相关注解解决映射问题，可以使用，或者 mybatis 开启下划线映射驼峰的规则，否则下划线映射驼峰会有问题
- `<association>/<collection>`: `/association_collection` association 标签在 resultMap 中的使用，通常表示 1 对 1 的关系。比如一个学生，只对应一个班级。collection 标签在 resultMap 中的使用，通常表示 list，即 1 对 n 的关系。比如一个班级对象中可能有一批学生
- `<sql>/<include>`: `/sql_include` 当多个 SQL 语句中存在重复的字段列表、表名、条件片段等时，<sql> 标签将这些重复的部分抽取为公共片段，include 标签引用 sql 标签定义的公共片段，将 SQL 片段动态插入到其他 SQL 语句中

## 3.6 mysql 相关
### 3.6.1 mysql 结合 mybatis 使用事务
`TransactionController` 中演示了 mysql 结合 mybatis 使用事务的操作

使用 `@Transactional` 表示该方法使用事务，数据源是默认的 mysql 主数据源。
当方法中抛出未捕获的异常时触发回滚，如果是 IOException/SQLException 等某些异常不会处罚回滚。
如果你在方法中 try catch 了这个异常，并且 catch 后不再往外抛出，则不会触发回滚，除非继续往外抛出，
发生回滚的不仅仅是 try catch 中的，而是整个方法内事务对应的数据源的数据库操作都会回滚

如果你不希望通过 `@Transactional` 注解的方式，或者希望当满足某一个条件时候(不一定是抛异常)，才手动提交事务，
那么你需要自己去操作 `DateSourceTransactionManager` 了，
如果 application.yml 常规配置好了 mysql 和 mybatis 那么 service 中直接通过 `@Autowire` 可以直接引用 `DateSourceTransactionManager` 了
操作 `DateSourceTransactionManager` 可以手动来提交事务，等价于方法上使用 `@Transactional` 注解

如果配置了多个 mysql 数据源后，对于方法的运行你启用事务，你想指定哪个数据源的事务进行回滚时
可以在 `@Transactional` 注解中指定事务管理器的 bean name

## 3.7 日志配置
`LogController` 中演示了 springboot 结合日志配置

日志级别：（由低到高）
- `trace`：最详细的日志级别，用于记录应用程序的运行轨迹，包括方法调用、变量值、异常信息等。
- `debug`：用于调试应用程序，记录开发人员在调试过程中感兴趣的信息。
- `info`：用于记录应用程序的正常运行信息，例如启动信息、配置信息、业务操作等。
- `warn`：用于记录警告信息，例如潜在的问题或异常情况，但不会导致应用程序崩溃。
- `error`：用于记录错误信息，例如应用程序运行时遇到的异常或错误情况。
- 
### 3.7.1 logback 日志(springboot 默认)
springboot 默认使用 logback，其日志框架在 spring-boot-starter 中的 spring-boot-starter-logging，所以无需额外引入其他依赖，项目中以演示 logback 配置接入为主
如果想使用 log4j2 日志框架，则需要先排除 Logback，再引入 spring-boot-starter-log4j2

logback 配置方式：
1. 通过文件配置 logback 日志，src/main/resources/logback-spring.xml 配置 logback-spring.xml
   - springboot 默认使用 logback，其日志框架在 spring-boot-starter 中的 spring-boot-starter-logging，所以无需额外引入其他依赖
   - springboot 项目会尝试加载 resources 下的 logback-spring.xml 和 logback.xml 文件，如果二者都不存在，则使用内置的默认 Logback 配置。logback-spring.xml > logback.xml 查找文件优先级
2. application.yml 直接可指定 logback xml 日志配置文件路径（但是也可以直接通过 application.yml 直接通过 logging 做 logback 日志简单配置）
3. 程序中上通过:
   - `@Slf4j` 注解，在类上添加该注解，即可在类中使用 log 日志，log 是 logback 日志框架的默认实现，后续代码使用 log 来打日志
   - 或者通过 `private final static Logger logger = LoggerFactory.getLogger(LogController.class);` 后续使用 logger 来打日志

logback 日志文件注意：
- 要配置异步日志输出，默认是同步会阻塞主业务

### 3.7.2 log4j2 日志(常用)
log4j2 配置方式：
和 logback 配置方式类似，只是 log4j2 配置文件是 log4j2.xml 或 log4j2-spring.xml，文件内容编写有差异

logback 和 log4j2 是有区别的，要注意的是：
- xml 配置大小写：Logback 日志小写标签名，Log4j2 日志大写标签名，Log4j2 的配置标签支持大小写混合，但官方推荐使用全大写
- Logback 配置是否比 Log4j2 更简单，但功能上 Log4j2 更强大
- log4j2 比 log4j 日志明显提升：支持异步日志，性能更强，支持运行时修改日志级别或配置，无需重启应用等
- 快速区分 log4j2 and log4j：`<Configuration>` 和 `<log4j:configuration>`

## 3.8 redis 使用
### 3.8.1 默认 lettuce 连接池
`LettuceController` 中演示了默认 lettuce 连接池的使用

基本介绍：
- redis 基本配置：在 application.yml 中配置完基本就可以拿 @Autowire 的 RedisTemplate 和 StringRedisTemplate 直接使用了。
- StringRedisTemplate 主要操作 StringRedisTemplate，RedisTemplate 主要操作其它。
- 如果有一些复杂配置，比如有第二个 redis 数据源或者 redis 想要配置序列化方式等，可以起一个 @Configuration 配置 RedisConnectionFactory 和 RedisTemplate 的 @Bean。
- springboot2 默认支持 lettuce redis 连接池，还有其他好用的比如 jedis 和 redission，它们都有着自己的特性，这里以 springboot 推荐的 lettuce 为配置例子演示。

lettuce 连接吃介绍：
- lettuce 连接池的特点是：一个 lettuce 连接可以被多个线程复用，即通过共享一个物理连接（shareNativeConnection=true）来实现多线程复用，不需要频繁创建连接，其实是通过 Netty 实现了异步非阻塞，单连接即可支持高并发。虽然多个线程可以共享一个 lettuce 的一个连接，但是 redis 它本身是串行执行命令的。
- 默认情况下（shareNativeConnection = true），Lettuce 的连接池（如 GenericObjectPool）实际上 不会生效，因为所有线程都共用一个连接。
- 如果你希望启用连接池（例如为每个线程分配独立连接），需要显式设置 shareNativeConnection = false，此时连接池参数（如 max-active、max-idle）才会起作用。
- 如果默认按照 shareNativeConnection = true，Lettuce 会始终使用一个物理连接，所有线程共享这个连接，不会生成第二个新的连接，连接池参数不生效，官方推荐默认值，因为已经适合了大多数场景，单个连接即可满足高并发需求，避免资源浪费。
- Lettuce 在不同线程间切换复用依赖于 Netty 的事件循环调度，而不是操作系统线程的切换。
- 如果高并发写入场景，共享连接可能导致事件循环队列过长，影响性能。此时可配置连接池，允许每个线程使用独立连接。
- shareNativeConnection 在哪配置呢？在 RedisConfig 中 RedisConnectionFactory 中配置即可，无法在 application.yml 中配置

代码示例中演示了 get/set，设置过期，操作 set/zset/hash/obj/geo 不同数据，以及 redis 自带的原子操作

## 3.9 异步操作
### 3.9.1 Async 注解
`AsyncController` 讲解了 springboot 中 `@Async` 的使用

经典异步的业务场景
* 1.用户注册（userThreadPool），比如注册时候，异步发送邮件，异步发送验证码
* 2.邮件发送（emailThreadPool）
* 3.短信通知（smsThreadPool）
* 4.数据同步（syncThreadPool）
* 5.报表生成（reportThreadPool）

怎么使用？
1. 创建一个配置类 `AsyncConfig` 需要标记 `@Configuration` 和 `@EnableAsync`，`@EnableAsync` 表示开启异步。这个配置类采用自定义线程池，这是生产中推荐的做法！下方代码中当你实现了 springboot 约定的 AsyncConfigurer 接口，实现其中 getAsyncExecutor() 方法，就等同于后续使用 @Async 时候告知其要使用这个线程池
``` java
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {
    /**
     * 自定义线程池配置
     *
     * @return 自定义 java.util.concurrent.Executor
     */
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);       // 核心线程数（始终存在的线程数）
        executor.setMaxPoolSize(10);       // 最大线程数（最大能开启几线程）
        executor.setQueueCapacity(25);     // 队列容量（队列中最多有多少个阻塞等待执行的任务）
        executor.setThreadNamePrefix("CustomAsync-"); // 线程名前缀
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); // 拒绝策略
        executor.initialize();
        return executor;
    }
}
```
2. 在一个业务方法上标记 `@Async` 即可

`@Async` 要注意的是：
- `@Async(线程池名)`，来指定线程池
- 异步结果可以用 CompletableFuture 接收

坑点！
- 直接使用，是简单场景，默认线程池的简单的方法异步，此方法会被异步执行，生产中不建议使用这种！因为 Springboot 中默认的线程池 SimpleAsyncTaskExecutor 太野蛮了，对于复杂场景容易导致 OOM
- 如果在异步方法所在的类中的其他方法调用此异步方法，不会产生异步效果。必须通过 Spring 容器获取 Bean 调用
- static 方法不能用 `@Async`：Spring 的代理机制无法处理 static 方法

## 4.0 MQ 消息队列使用
关键词：主题，生产者，消费组，消费者，分区，key，偏移量

生产到接收消息大致过程：
- 「生产者」 生产 消息，消息发送到 kafka 集群 =>
- mq 集群将消息放在某一个 partition 分区中，依据 key 判定放在哪个分区，发送到消费者组 =>
- 该消息只能被消费者组中的一个消费者消费，但是不同的消费组都可以消费该消息

生产到接收消息详细过程（以 kafka 来说）：
* 1.生产者生产消息发往 kafka 集群 topic，发送指定 topic 和 msg
* 2.kafka 集群依据 key 将消息放入依据 key 计算出来的分区，key 可由消费者发送
* 3.kafka 默认策略，一旦 kafka 集群配置好消费组和消费者，消费组中具体哪个消费者消费具体哪一个分区就已经确定好了
* 4.同一个 topic 的消息，不同消费组都可以消费，但是同一个消费组中只有一个消费者可以消费，消费指定 topic 和消费组
* 5.消费者消费默认是自动提交偏移量，消费 5s 后自动告诉 kafka 消息被消费了，下次无需再发送了
* 6.消费者手动提交偏移量，需要 application.yml 配置支持手动提交偏移量，且配置 Configuration 中指定 kafka 的 bean，然后编写方法和注解，注解指定这个 kafka 提交方式的 bean，代码中通过 `ack.acknowledge();` 手动提交偏移量

如果有 A，B 两个消费组，其中 A 消费组有 C1，C2 两个消费者，那么当 kafka 集群配置好消费组（包括其中消费者）时，那么 kafka 就已经定好哪几个分区固定发给消费组 A 中的 C1，那几个分区未来始终固定发给消费组 B 中的 C2

### 4.0.1 kafka
`kafkaController` springboot 中消息队列的使用

示例演示了：
- 生产者：常规发送消息
- 生产者：发送带有 key 的消息
- 生产者：批量发送消息
- 消费者：常规消费消息

代码怎么配置：
1. `application.yml` 中配置 kafka 连接信息等内容
2. 业务代码直接通过注入 `KafkaTemplate` 可以直接操作生产和消费消息了 
3. 但是要注意，如果要手动提交偏移量，`application.yml` 中需要开启配置支持手动提交，然后就需要创建一个配置类 `KafkaConfig` 需要标记 `@Configuration`，在其中配置 kafka 手动提交偏移量来确认消息消费 

## 4.1 ioc/aop
spring 的经典哲学
### 4.1.1 ioc
`IocController` IOC (Inversion of Control) 控制反转

什么叫控制反转：
* 在传统编程中，开发者控制对象的创建，而在 IOC 模式下，这个控制权被"反转"给了 Spring 容器，spring 容器来管控对象，来管控他们什么时候注入
* 它不是具体的技术，而是一种设计原则，用来降低代码耦合度
### 4.1.2 aop
`AopController` 面向切面编程 Aspect-Oriented Programming

AOP的核心价值：
- 关注点分离：将横切关注点(日志、事务、安全等)与业务逻辑分离
- 代码复用：避免重复代码，一处定义，多处应用
- 非侵入性：不修改原有业务代码，通过配置即可增强功能
- 可维护性：集中管理横切逻辑，修改时只需调整切面代码

aop 在实际中往往结合自定义注解来使用

这里举出了一个例子，比如日志 aop 切面，具体做法是：
1. 先自定义一个注解 `LogOperation`
2. 然后定义一个切面类 `OperationLogAspect`，通过 `@Aspect` 标记是一个切面，然后定义定义切入点，以及定义方法执行什么阶段时候去切入
``` java
@Aspect
@Component
@Slf4j
public class OperationLogAspect {
    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    // 定义切入点：所有使用了 @LogOperation 注解的方法
    @Pointcut("@annotation(logOperation)")
    public void logPointcut(LogOperation logOperation) {
    }

    /**
     * @Around 通过 joinPoint.proceed()，因此能在执行目标方法前和后执行你要做的操作。往往去做日志记录、事务管理、性能监控
     * @Before 方法执行前，往往去做权限校验、参数校验
     * @After 方法执行后，往往去做资源清理、审计
     * @AfterReturning 方法成功返回后
     * @AfterThrowing 方法抛出异常后
     */
    // 环绕通知
    @Around("logPointcut(logOperation)")
    public Object logAround(ProceedingJoinPoint joinPoint, LogOperation logOperation) throws Throwable {
        long start = System.currentTimeMillis(); // 方法执行前的时间

        try {
            // 执行目标方法（核心！）
            Object result = joinPoint.proceed();
            // 存储 log 日志
            saveLog(joinPoint, logOperation, result, null, start);
            return result;
        } catch (Exception e) {
            // 存储 log 日志
            saveLog(joinPoint, logOperation, null, e, start);
            throw e;
        }
    }
    
    // ... 后续代码
}
```
3. 然后在具体业务代码方法上使用 `@LogOperation(value = "依据姓名查询学生", operationType = "search", saveRequestParams = true)` 即可完成切入

## 4.2 鉴权
### 4.2.1 jwt 平台登录
`JwtController` jwt 登录认证，基于 token 的登录认证，无状态，可扩展

项目中如何使用 jwt？
- 1.编写一个可被 spring 容器管理的 jwt util 工具类（我这里写的诗 jwt service，逻辑一样的），来实现 jwt 的 token 生成，以及 token 检验
- 2.然后编写一个被 spring 容器管理的 auth 拦截器，来拦截请求，在请求之前通过 HttpServletRequest 获取请求头中的 token，并且做校验，成功则放行，否则返回
- 3.把这个拦截器注册进行注册，其中会配置拦截哪些路径
- 4.当业务 login 进入后，被拦截器放行（基于拦截器配置），会先进行注册生成 jwt token，然后后续请求进来，通过拦截器拦截，被 jwt 工具类来校验

业务实际请求过来的效果：
- login 平台登录接口：进行账密校验，成功则生成 token 返回前端，前端把 token 保存到 localstorage 中
- 后续接口请求：通过 AuthInterceptor 拦截器来拦截每个请求，通过拦截器获取 HttpServletRequest 中的 req header 中的 token，再来验证 token 是否有效，来决定让请求是否继续执行

### 4.2.2 auth2.0 第三方登录
`Auth2Controller` 项目中有演示 基于 oauth2.0 + security 常见于第三方登录，其方式常用语比如微信，微博，github 授权登录场景

项目中怎么用？
- yml 中需要配置 security oauth2
- `Security2Config` 标记了 `@Configuration` 和 `@EnableWebSecurity` 注解，其本质是一个众多过滤器的过滤链条。配置类中需要指定：
  - 第三方授权登录成功后，让浏览器重定向到哪个业务接口
  - 配置哪些接口必须要经过权限认证校验
- 然后实际登录请求过来，业务后端返回让浏览器重定向到第三方授权地址
- 如果授权完后，它会按照 `Security2Config` 配置进行重定向到某个业务接口，并且携带上鉴权信息（之前，业务后端通过和第三方交互从第三方那拿到一些信息后封成鉴权信息返给浏览器）
- 如果后续业务请求也会带上鉴权信息，经过 `Security2Config` 众多 security 过滤器，被过滤器检验鉴权，不通过就直接打回，通过就继续后续业务逻辑

oauth2.0 + security 实现第三方登录的大致过程（下放中的连接和关键 key 可以参考下处 yml 中的配置）：
- 1.【访问业务-重定向三方地址】：浏览器访问 /login → 业务返回，用户点登录按钮 → 微信授权页
- 2.【访问三方-重定向业务security接口】：浏览器上用户授权后 → 第三方系统重定向回 http://your-domain.com/login/oauth2/code/wx?code=CODE 浏览器来展现。redirect-uri 来决定这个重定向的地址
- 3.【访问业务-重定向三方登录后要跳转的地址】：浏览器访问 http://your-domain.com/login/oauth2/code/wx?code=CODE → 打到业务服务端，业务服务端 security 会自动处理它！此接口不需要进行 controller 层的实现！
  - 3.1 访问 /login/oauth2/code/wx 后，发现 registrationId="wx"，从配置中取出 provider.wx 的参数
  - 3.2 `[code => access_token]`: 用 code 参数向第三方平台 wx 请求（请求地址来自配置 token-uri），获取 access_token（无需你写 HTTP 客户端代码，自动处理）
  - 3.3 `[access_token => 用户信息]`: 用 access_token 再向 wx 发送请求（请求地址来自配置 user-info-uri），来获取用户信息
    ``` json
    {
     "openid": "OPENID",
     "nickname": "张三",
     "sex": 1,
     "province": "广东",
     "city": "广州",
     "country": "中国",
     "headimgurl": "https://example.com/head.jpg",
     "privilege": [],
     "unionid": "UNIONID"
    }
    ```
  - 3.4 Spring Security 将用户信息（包含 openid）存入 OAuth2AuthorizedClient（所以后续业务接收的请求你在 controller 方法中 @RegisteredOAuth2AuthorizedClient("wx") OAuth2AuthorizedClient client 注入用户数据就能直接拿到实例）
  - 3.5 Spring Security 将用户信息存入 SecurityContext 安全上下文中，通过 SecurityContextHolder 保存上下文
  - 3.6 依据 Security2Config 的配置，发给浏览器让其重定向到 /user
  - 3.7 通过 SecurityContextPersistenceFilter 将 SecurityContext 存储到 Session，同时，Spring Security 会设置一个 Cookie（默认名称：SESSION）到 HTTP 响应头：（Set-Cookie: SESSION=abc123xyz; Path=/; HttpOnly; Secure）
- 4.【访问业务-返回业务结果】：浏览器其实这里拿到了 cookie（上一步响应头中的），以及重定向的 /user 地址（你配置的 /user），业务服务端接收请求
  - 4.1 这个业务请求过来，其实已经带上了鉴权信息了，Security2Config 本质是一个过滤器的过滤链，其中的关键过滤器会处理权限的校验，校验不过返回 401 Unauthorized

对应 application.yml 中配置如下，由于 yml 中已经配置来 sso 相关，所以没有配置上下面这块内容：
``` yml
spring:
  security:
    oauth2:
      client:
        registration:
          wx:  # 自定义注册名（可任意，但需和代码一致）
            # wx 平台注册给你的
            client-id: your-wechat-appid  # 替换为你的微信AppID
            client-secret: your-wechat-appsecret  # 替换为你的微信AppSecret

            redirect-uri: "{baseUrl}/login/oauth2/code/wx"  # 回调地址（需在微信平台注册）：即第三方平台授权(登录)通过之后让浏览器重定向的地址

            # redirect-uri 这个接口打到业务服务端会带有 code，会请求 token-uri 地址，目的地是将 code 传入，为了拿到 token，传递到其中的一个入参
            authorization-grant-type: authorization_code

            scope: snsapi_login  # 微信固定Scope

            # redirect-uri 中尾部是 wx 的配置。当授权通过后，浏览器发送 redirect-uri 到业务服务端，获取 wx 相关数据，即下方
            provider:
              wx:
                authorization-uri: https://open.weixin.qq.com/connect/qrconnect
                # redirect-uri 这个接口打到业务服务端会带有 code，会请求这个地址，目的地是将 code 传入，为了拿到 token
                token-uri: https://api.weixin.qq.com/sns/oauth2/access_token
                # 获取 access token 后，用 access token 向 user-info-uri 发送请求，获取用户信息
                user-info-uri: https://api.weixin.qq.com/sns/userinfo
                # 向 user-info-uri 发送请求时，需要携带此信息
                user-name-attribute: openid  # 关键！微信返回的用户唯一标识字段
```
### 4.2.3 公司层级 sso
刚才介绍了 oauth2.0 + security 实现第三方登录的大致过程
公司的 sso 会有些类似这套机制，可以看 `SsoController`

sso，单点登录，多个平台/系统的统一登录，跨平台有效
一般公司业务会有自己的 sso 认证中心，一般只需要在配置文件中配置上公司认证中心连接等内容即可，不需要使用购买的公有云
OAuth2.0/OpenID Connect：当前最主流方案，适合现代微服务架构，其被实现的具体产品有：
- Keycloak
- Auth0
- AWS Cognito
- Okta
- Azure AD

Keycloak 是一个开源的身份和访问管理(IAM)解决方案,提供了单点登录(SSO),它支持多种协议，包括OAuth2.0、OpenID Connect、SAML等，是企业级应用中常用的认证授权中心
- 初次访问：用户访问受保护的资源，如/user/profile
- 认证检查：Spring Security 检测到用户未认证
- 重定向到认证中心：用户被重定向到 Keycloak 登录页面
- 用户登录：用户在 Keycloak 界面上输入凭证
- 授权码返回：Keycloak 验证用户凭证后，重定向回应用，携带授权码
- 令牌交换：Spring Security 后台使用授权码向 Keycloak 请求访问令牌
- 用户信息获取：使用访问令牌获取用户信息
- 创建认证对象：基于用户信息创建Authentication对象

项目中怎么配置 OAuth2.0/OpenID Connect：
1. yaml 中配置 sso
2. 配置 `SecurityConfig` 配置类，它决定了哪些请求过来要被鉴权，以及授权成功后重定向到哪个业务页面，以及登出后的重定向等内容
3. 后续业务请求过来会带上鉴权信息，能被过滤，请求 controller 中的 `@AuthenticationPrincipal OAuth2User principal` 也会被自动注入

项目中使用 OAuth2.0/OpenID Connect 实际请求访问过程（整体过程跟上头讲解的 auth2.0 比较相似）：
- 第一步：未登录访问业务
- 第二步：Keycloak认证
- 第三步：认证回调处理
- 第四步：已认证访问业务

## 4.3 多种额外数据库(除 mysql/redis)
### 4.3.1 mongodb 使用
- `MongoDbSimpleController` 使用 dao 方式操作 mongodb
- `MongoDbComplexController` 使用注入 template 方式来操作 mongodb
- `MongoDbGeoController` mongodb 的地理位置操作

mongodb 常用的场景：总结： MongoDB 最适合那些数据结构相对灵活、读写密集、需要快速开发迭代的应用场景
- 内容管理系统 (CMS)：博客文章，每篇文章的内容结构可能不同
- 日志和事件数据存储：高写入性能，无需预定义 schema
- 电商商品目录：支持复杂的嵌套结构，查询灵活
- 实时分析和报告：支持复杂的聚合查询，水平扩展容易

另外 mongodb 在地理位置查询时候非常方便

怎么使用：
- yml 中配置 mongodb 连接
- 通过编写 dao 层 `@Repository` 接口 extends MongoRepository，注意接口的方法无需实现，使用 Spring Data 的命名规则即可，会默认自动实现这些方法
``` java
@Repository // 相当于MyBatis中的@Mapper
public interface ProductRepository extends MongoRepository<Product, String> {

    // 方法名查询 - Spring Data会自动解析方法名生成查询
    List<Product> findByName(String name);

    List<Product> findByCategory(String category);

    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    List<Product> findByCategoryAndPriceLessThan(String category, Double maxPrice);

    // 模糊查询
    List<Product> findByNameContainingIgnoreCase(String name);

    // 自定义查询 - 使用MongoDB查询语法
    @Query("{'price': {$gte: ?0, $lte: ?1}}")
    List<Product> findProductsByPriceRange(Double minPrice, Double maxPrice);

    @Query("{'tags': {$in: [?0]}}")
    List<Product> findByTag(String tag);

    // 统计查询
    long countByCategory(String category);
}
```
- 然后后续业务代码正常调用这些接口中的方法即可操作 mongodb

为什么 Repository 接口不需要实现类的原因？
因为 Spring Data 使用了代理模式和 AOP，Mongo 又是在 Spring Data 中的，当你 service 中直接注入 ProductRepository 后，Spring 启动时扫描所有继承 MongoRepository 的接口，为每个 Repository 接口动态生成实现类，解析方法名（如findByCategory）并转换为 MongoDB 查询，
所以 Spring Data 的查询方法命名有一套规则，如果不遵循 Spring Data 命名规则就会在 springboot 项目启动时候就会发生报错

Spring data 命名规则：
- findBy + 字段名: `List<Product> findByName(String name);`
- And/Or组合: `List<Product> findByNameAndCategory(String name, String category);`
- 比较操作: `List<Product> findByPriceGreaterThan(Double price);`
- 模糊匹配: `List<Product> findByNameLike(String name);`

当然简单场景你可以像上头通过编写 dao 层 `@Repository`，如果复杂的数据操作场景注解注入 MongoTemplate 来操作吧
- 实际开发中 80% 的场景使用 Repository 接口（简单、快速、易维护）。Repository，代码简洁，约定优于配置，对应简单 CRUD
- 20% 复杂场景使用 MongoTemplate（灵活性、性能）。MongoTemplate，更灵活，支持复杂查询和聚合操作，运行时构建查询条件，可以精确控制查询行为，适合复杂查询/动态查询/性能优化

### 4.3.2 elastic search 使用
- `EsSimpleController` 使用 dao 方式操作 es
- `EsOrdinaryController` 使用注入 template 方式来操作 es

es 经典常用业务场景：
- 全文搜索（电商/内容平台）
- 日志分析与监控（ELK栈）即 es + Logstash + Kibana
- 直接通过 springboot 中 Logback 对应 xml 日志配置，配置上发送到 ES
- es 存储
- kibana 可视化查询
- 实时数据分析和可视化
- 个性化推荐系统
- 自动补全和搜索建议

版本注意：
Spring Boot 2.4+ 与 Elasticsearch 7.x 兼容，
Spring Boot 3.x 与 Elasticsearch 8.x 兼容

springboot 项目中怎么接入 es，其实和 mongodb 类似：
- yml 配置 es 连接
- 简单场景看 `EsSimpleController`，使用自定义 dao，复杂场景使用注入 `ElasticsearchRestTemplate`

### 4.3.3 hive 使用
`HiveController` 中有样例

hive 作为 hadoop 生态的一员，直接在 Spring Boot 应用中频繁访问 Hive 通常不是最佳实践, 因为 Hive 设计初衷是 `[面向批处理]` 而非 `[实时查询]`

【hive 为什么不要是实时查询/写入！】
- hive 相比 mysql 查询速度较慢, 因为 hive 是基于 Map/Reduce 的, 而 mysql 是基于磁盘的，一个简单，
- 假设一个简单查询，Hive 执行占用内存很大，cpu 时间很多，而 mysql 执行占用内存很小，cpu 时间也很少，
- 所以如果 hive 来承接实时的大流量的查询，你的集群资源很容易耗尽，连接 hive 查询耗时久，连接池耗尽，新请求排队，服务 OOM，上游服务连锁出现故障，服务雪崩，应用很快卡死

【业务开发如何存储到 hive】不能每次一个个写入 hive，每次写入都会造成产生 1/多个 HDFS 文件，长期这样下来，会产生太多小文件，然后 NameNode 就会崩溃，所以要成批成批的写入，减少写入频次
- 一般业务开发拿到业务数据后，代码中发往 kafka
- kafka 拿到消息后，然后编写 Spark Structured Streaming 的作业代码（scala）（大公司可能有这样的平台来抽取，就不需要写这样的脚本代码了）来消费 Kafka 数据，构成一定时间段成批的数据，再成批写入 hive
- Airflow 调度系统 + Shell 脚本来定时合并 hive 表中的小文件，优化存储和查询性能（因为前面即使一定时间成批写入 hive，其实还是有小文件问题，但不像实时写入那么严重，也还需要合并）（大公司可能有这样的大数据平台，员工只需关心如何平台上配置消息落入 hive，以及后续 hive 的 ETL 行为）

【业务开发一般怎么查询 hive】
- hive 每天定时同步数据到 mysql
- 业务代码查询 mysql，但会有 T+1 延迟

项目中怎么配置 hive：
1. 直接配置类中 `HiveConfig` 中配置连接
2. 后续业务中使用

### 4.3.4 hbase 使用
`HbaseController` 中展示 hbase 使用

hbase 基本介绍：
- MySQL 一行数据的所有列存储在一起，HBase 同一列族的所有列值存储在一起，不同列族分开存储。HBase 是列式存储数据库，是面向列族 (Column Family) 的列式存储，不是简单的按列存储
- HBase 特别适合：高并发读写，存储稀疏数据（很多列可能为空），按列查询效率高，水平扩展能力强
- RowKey 是 HBase 中最重要的概念之一，它不是索引，而是数据组织的核心，RowKey 是表中每条记录的唯一标识符，类似于关系数据库的主键
- RowKey 是设计表时就必须确定

hbase/hive 相同：
- hbase 和 hive 类似都是基于 HDFS 存储的，都是属于 hadoop 生态中的，但是 hive 是离线表，不可实时查询，而 hbase 可以实时去查询
- 二者都可以用来存储诸如用户行为数据，点击事件等

那为啥不把数据都给 hbase 存储呢？
- 虽然 hbase 看似很强大，但是用 hbase 存储 hive 的数据就像跑车去拉快递，因为 HBase 每 TB 数据需要 3-4 台服务器，而 hive 只需要 1 台。比如某 10TB 用户行为数据，存储到 hbase 估计要 40 台高性能服务器，每月花费 21w/月，而如果最近 7 天热点数据存储到 hbase 中，非热点数据存储到 hive 中，估计成本只有 7w/月，成本大幅降低。而且 hbase 实时查询要注意如果扫描大量数据比如亿级行数可能会导致 hbase 集群崩掉，而 hive 查询时候扫描亿级行数一般不会有问题
- HBase 处理"现在发生了什么"，Hive 分析"过去发生了什么"
- 适合在HBase上执行SQL的场景：
  - 简单点查：`SELECT * FROM table WHERE rowkey = 'xxx'`
  - 有限范围扫描：`SELECT * FROM table WHERE rowkey LIKE 'user123_%' LIMIT 1000`
  - 计数器查询：`SELECT total FROM counters WHERE date = '2024-01-15'`
  - 时间窗口查询：`SELECT * FROM events WHERE rowkey BETWEEN '20240115' AND '20240116'`

项目中如何配置 hbase：
1. 配置 `HbaseConfig` 配置类，直接进行连接
2. 后续业务中直接注入使用 `Connection` 即可操作 hbase 数据库

需要注意：绝对不要在HBase上执行的SQL：
- 全表聚合：`SELECT COUNT(*), AVG(value) FROM huge_table`
- 多表JOIN：`SELECT u.*, o.* FROM users u JOIN orders o ON u.id = o.user_id`
- 模糊查询：`SELECT * FROM table WHERE name LIKE '%john%'`
- 大偏移分页：`SELECT * FROM table ORDER BY time LIMIT 10 OFFSET 1000000`


### 4.3.5 clickhouse 使用
`ClickHouseController` ClickHouse 在 日志分析、实时报表、行为分析 中非常常用。

hadoop 生态中的一员，一般项目中只做查询操作，写入操作通过 ETL 将比如 hive 中数据同步到 clickhouse，ETL 一般公司有专门的平台提供，同步因为 hive 查询非常慢，且一般都不会在项目中查询，clickhouse 数据查询很快，适合在业务代码中进行查询

项目中如何使用：
1. `ClickHouseConfig` 配置类中进行配置连接，生成 `JdbcTemplate` bean
2. 后续通过注入使用 `JdbcTemplate` 操作 clickhouse

大数据生态的数据 hive/hbase/clickhouse 都不支持 yml 的直接配置
mysql，redis，mongodb，es 是支持 yml 的直接配置的

## 4.4 spring cloud 微服务
`MicroServiceController` 展示了作为调用方服务，怎么使用微服务

作为调用方如何配置使用：
1. yml 中配置自己的服务名，和注册中心，服务启动将自己向注册中心注册
2. 配置 `RestTemplateConfig` 生成 `RestTemplate` bean，其中通过启用 Ribbon 负载均衡，以此能通过服务名来调用下游微服务的接口，而不是 ip 来调用
3. 在业务代码中用注入的 `restTemplate` 来调用下游微服务接口
``` java
// 使用服务名"user-service"，而不是IP:端口，来访问 user-service 用户服务中的 users 接口
restTemplate.getForObject("http://user-service/users", String.class);
```

作为服务方如何配置使用：
1. 和调用方 yml 一样的配置，yml 中配置自己的服务名，和注册中心，服务启动将自己向注册中心注册
2. 直接编写微服务，其实就是 controller 上没带映射路径。启动后 user-service 已注册
``` java
@RestController
public class UserController {
    @GetMapping("/users")
    public String getUsers() {
        return "用户列表"; // 实际返回数据
    }
}
```

## 4.5 SSE/CMD 业务处理客户端交互：音视频流
`MediaCallbackController` 中演示了实际业务中客户端，多媒体服务，和自己（业务服务）之间怎么交互的

当存在客户端开发，多媒体服务能力，以及业务服务能力，在音视频信息场景下三者如何交互：
客户端 (SEI) -> 多媒体服 (回调业务 HTTP 接口) -> 业务 HTTP 接口承接 (CMD 命令) -> 客户端

详细描述：
1. 客户端对多媒体发送 SEI 信息
2. 多媒体处理 SEI 信息，并且回调一个业务服务的 http 接口，然后把关键信息带给业务（业务 http 接口如下 `/sei/callback`）
3. 业务此回调接口 `/sei/callback` 中进行一些业务处理
``` java
// 业务 http 接口 /sei/callback
@PostMapping("/sei/callback")
public String handleSeiCallback(@RequestBody SeiCallbackRequest request) {
    log.info("收到SEI回调数据: {}", request);

    // 1. 验证签名，防止未授权调用
    if (!validateSignature(request)) {
        return "签名验证失败";
    }

    // 2. 根据SEI类型处理不同业务
    switch (request.getSeiType()) {
        case "GIFT":
            System.out.println("收到 GIFT SEI 数据，执行相应业务处理逻辑");
            // 发送 CMD 命令给客户端（WebSocket）
            commandService.broadcastToRoom("123", "GIFT_ANIMATION", request.getSeiPayload());
        case "CHAT":
            System.out.println("收到 CHAT SEI 数据，执行相应业务处理逻辑");
            // 发送 CMD 命令给客户端（WebSocket）
            commandService.sendToUser("123456789", "CHAT_MESSAGE", request.getSeiPayload());
        case "INTERACTION":
            System.out.println("收到 INTERACTION SEI 数据，执行相应业务处理逻辑");
        default:
            return "不支持的SEI类型";
    }
}
```
3. 其中会执行 cmd 命令向客户端发送消息。执行 cmd 命令本质是因为前期业务服务侧已经和客户端建立了 websocket 连接了，项目中通过 `WebSocketConfig` 做一个配置类，service 层通过注入 `messagingTemplate` 即可操作向客户端发送 cmd 命令来通信
``` java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // 灵活配置 WebSocket 端点，客户度通过什么 URL 连接
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 配置 WebSocket 端点，客户端通过这个 URL 连接
        registry.addEndpoint("/ws")  // 1. 客户端连接的地址
                .setAllowedOriginPatterns("*")  // 允许所有跨域
                .withSockJS();  // 启用SockJS降级
    }

    // 配置发送消息的前缀
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 配置服务端可以推送消息的前缀
        registry.enableSimpleBroker("/topic", "/queue");  // 广播和点对点前缀

        // 配置客户端发送消息的前缀
        registry.setApplicationDestinationPrefixes("/app");

        // 配置点对点消息前缀
        registry.setUserDestinationPrefix("/user");
    }
}
```
4. 该 http 最后返回响应结果给多媒体服务侧