package org.example.springbootdemo.controller.mybatis;

import com.alibaba.fastjson.JSON;
import org.example.springbootdemo.dto.SchoolClassStudentDTO;
import org.example.springbootdemo.dto.StudentSchoolClassDTO;
import org.example.springbootdemo.mapper.SchoolClassMapper;
import org.example.springbootdemo.mapper.StudentMapper;
import org.example.springbootdemo.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 各样的 sql 对应的 mapper xml 的写法举例
 *
 * 增删改查：select，insert，update，delete
 * 常用：if，where，set，foreach，parameterType，resultMap，resultType，sql，include
 * 条件：choose，when，otherwise
 * 不是很常用：trim，bind，association，collection
 */
@RestController("/muti-sql-xml")
public class MutiSqlXmlController {
    @Autowired
    public StudentMapper studentMapper;
    @Autowired
    public SchoolClassMapper schoolClassMapper;

    // <select>
    // 查询
    @GetMapping("/select")
    public Student select() {
        return studentMapper.selectStudentById(1L);
    }

    // <insert>
    // 插入
    @GetMapping("/insert")
    public String insert() {
        Student student = new Student();
        student.setName("liming");
        student.setAge(12);
        studentMapper.insertStudent(student);
        return "success";
    }

    // <update>
    // 更新
    @GetMapping("/update")
    public String update() {
        Student student = new Student();
        student.setName("liming");
        student.setAge(12);
        studentMapper.updateStudent(student);
        return "success";
    }

    // <delete>
    // 删除
    @GetMapping("/delete")
    public String delete() {
        studentMapper.deleteStudentById(1L);
        return "success";
    }

    // <if>
    // 根据条件判断 sql 中需要展示什么内容
    @GetMapping("/if")
    public String ifTag() {
        Student student = new Student();
        student.setIsDeleted(false);
        studentMapper.insertStudentIfTag(student);
        return "success";
    }

    // <where>
    // where 标签比如在查询时，能智能的处理 and 或者 or 关键字，并且当 where 标签中真的有条件语句时候，这个 where 关键字才会被灵活的生成否则不生成
    @GetMapping("/where")
    public Student whereTag() {
        String name = "liming";
        Integer age = 12;
        return studentMapper.selectStudentWhereTag(name, age);
    }

    // <set>
    // set 标签会职能的自动处理末尾多余的逗号
    @GetMapping("/set")
    public String setTag() {
        Student student = new Student();
        student.setName("liming");
        student.setAge(12);
        studentMapper.updateStudentSetTag(student);
        return "success";
    }

    // <choose>/<when>/<otherwise>
    // 类似 java 中的 switch case 语句，choose 类似 switch，when 类似 case，otherwise 类似 default
    // <choose> 结构天生适合处理互斥条件。如果条件不是互斥的，使用多个 <if> 可能更合适
    @GetMapping("/choose_when_otherwise")
    public Student chooseTag() {
        String name = "liming";
        Integer age = 12;
        return studentMapper.selectStudentChooseTag(name, age);
    }

    // <foreach>
    // 实现遍历，比如 in 某个 list
    @GetMapping("/foreach")
    public Student foreachTag() {
        List<String> names = new ArrayList<String>() {
            {
                add("liming");
                add("zhanghua");
            }
        };
        return studentMapper.selectStudentForeachTag(names);
    }

    // <trim>
    // 它可以通过添加或移除内容的前缀、后缀，或者替换掉多余的关键字（如 AND、OR、逗号等）
    @GetMapping("/trim")
    public Student trimTag() {
        String name = "liming";
        return studentMapper.selectStudentTrimTag(name);
    }

    // <bind>
    // 用于在 OGNL 表达式上下文中创建一个变量，并将其绑定到当前上下文中，通常用于简化动态 SQL 中的复杂表达式、避免重复计算
    @GetMapping("/bind")
    public Student bindTag() {
        String name = "liming";
        return studentMapper.selectStudentBindTag(name);
    }

    // <parameterType>
    // parameterType 一般情况下可以省略不写，mybatis 能自动推断出来类型，但是写上能更直观清晰，推荐写生的比如当入参是 pojo 实体类
    /*
        parameterType 能自动推断入参的数据类型，比如：
         - 基本数据类型：int，long，boolean 等
         - 包装数据类型：Integer，Long 等
         - String 类型
         - 自定义的 pojo 对象：比如 Student，User 等
         - 集合类型：比如 List，Map 等
    * */
    @GetMapping("/parameterType")
    public String parameterTypeTag() {
        Student student = new Student() {
            {
                setName("liming");
                setAge(12);
                setSchoolClassId(2L);
            }
        };
        studentMapper.insertStudent(student);
        return "success";
    }

    // <resultMap>, <resultType>
    // resultMap 先定义好 数据库 column 怎么 映射到 对象中的 field 中
    // resultType 如果是 mybatis plus 使用其相关注解解决映射问题，可以使用，或者 mybatis 开启下划线映射驼峰的规则，否则下划线映射驼峰会有问题
    @GetMapping("/resultMap_resultType")
    public String resultMapResultTypeTag() {
        // resultMap
        Long studentId = 1L;
        Student student = studentMapper.selectStudentById(studentId);
        System.out.println("student:" + JSON.toJSONString(student));
        // resultType
        /*
            一般开发用 mybatis，最常用 resultMap，因为 resultType 映射数据库 column 下划线字段 => Object 小驼峰 filed 默认无法映射过去
            比如查询出来的数据字段是下划线，mybatis 默认无法通过 resultType 映射到小驼峰到 Student 类变量中，解法是：
            1.方式一：resultMap 声明映射（企业级开发更倾向于这种方式）
            2.方式二：mybatis 开启下划线到驼峰的映射
                mybatis:
                    configuration:
                        map-underscore-to-camel-case: true
            3.方式三：sql 编写给字段起别名，比如 student_id as studentId，这样 column alias: studentId => field: studentId 可以成功
        * */
        return "success";
    }

    // <association>, <collection>
    // <association> 标签在 <resultMap> 中的使用，通常表示 1 对 1 的关系。比如一个学生，只对应一个班级
    // <collection> 标签在 <resultMap> 中的使用，通常表示 list，即 1 对 n 的关系。比如一个班级对象中可能有一批学生
    @GetMapping("/association_collection")
    public String associationCollectionTag() {
        // association 1 对 1
        Long studentId = 1L;
        StudentSchoolClassDTO studentSchoolClassDTO = schoolClassMapper.selectStudentById(studentId);
        System.out.println("studentSchoolClassDTO:" + JSON.toJSONString(studentSchoolClassDTO));
        // collection 1 对 n
        Long schoolClassId = 2L;
        SchoolClassStudentDTO schoolClassStudentDTO = schoolClassMapper.selectSchoolClassById(schoolClassId);
        System.out.println("schoolClassStudentDTO:" + JSON.toJSONString(schoolClassStudentDTO));
        return "success";
    }

    // <sql>, <include>
    // 当多个 SQL 语句中存在重复的字段列表、表名、条件片段等时，<sql> 标签将这些重复的部分抽取为公共片段，<include> 标签引用 <sql> 标签定义的公共片段，将 SQL 片段动态插入到其他 SQL 语句中
    @GetMapping("/sql_include")
    public Student sqlIncludeTag() {
        Long studentId = 1L;
        return studentMapper.selectStudentIncludeTag(studentId);
    }
}
