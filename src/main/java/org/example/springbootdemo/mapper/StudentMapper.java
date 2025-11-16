package org.example.springbootdemo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.springbootdemo.dto.StudentSchoolClassDTO;
import org.example.springbootdemo.model.SchoolClass;
import org.example.springbootdemo.model.Student;

import java.util.List;

@Mapper
public interface StudentMapper {
    /** ============== 基础增删改查 ============== **/
    // select
    // @Param 为了在 xml 文件中的占位符提供名称 student_id
    // 如果没有配置，或按照参数的顺序，通过 arg0, arg1, arg2 ... 这样的占位符在 xml 展现
    Student selectStudentById(@Param("student_id") Long studentId);

    // insert
    // 返回的是操作影响的行
    int insertStudent(Student student);

    // update
    // 返回的是操作影响的行
    int updateStudent(Student student);

    // soft delete
    // @Param 为了在 xml 文件中的占位符提供名称 student_id
    // 返回操作影响的行
    int updateStudentIsDeletedById(@Param("student_id") Long studentId);

    // solid delete
    // @Param 为了在 xml 文件中的占位符提供名称 student_id
    // 返回删除的行
    int deleteStudentById(@Param("student_id") Long studentId);

    /** ============== 分页查询 ============== **/
    // select page
    // 依据 name 对 student 表做分页查询，使用 pagehelper 插件
    List<Student> selectStudentByPage(@Param("name") String name);

    // select page
    // 依据 name 对 student 表做分页查询，使用 limit 条件
    List<Student> selectStudentWithLimit(@Param("name") String name, @Param("offset") int offset, @Param("pageSize") int pageSize);

    /** ============== 联表查询 ============== **/
    // select student left join school_class
    // 如果联合查询的业务逻辑更偏向于表一，应将方法放在表一的 Mapper 中
    // 如果联合查询涉及多个业务模块，建议创建一个独立的 Mapper
    // 如果联合查询的业务逻辑更偏向于表二，可以放在表二的 Mapper 中，但需谨慎评估业务归属
    List<StudentSchoolClassDTO> selectStudentSchoolClass(@Param("name") String name);

    // select student full join school_class
    // 如果联合查询的业务逻辑更偏向于表一，应将方法放在表一的 Mapper 中
    // 如果联合查询涉及多个业务模块，建议创建一个独立的 Mapper
    // 如果联合查询的业务逻辑更偏向于表二，可以放在表二的 Mapper 中，但需谨慎评估业务归属
    List<StudentSchoolClassDTO> selectStudentSchoolClass2(@Param("name") String name);

    // select student inner join school_class
    // 如果联合查询的业务逻辑更偏向于表一，应将方法放在表一的 Mapper 中
    // 如果联合查询涉及多个业务模块，建议创建一个独立的 Mapper
    // 如果联合查询的业务逻辑更偏向于表二，可以放在表二的 Mapper 中，但需谨慎评估业务归属
    List<StudentSchoolClassDTO> selectStudentSchoolClass3(@Param("name") String name);

    // select student = school_class
    // 如果联合查询的业务逻辑更偏向于表一，应将方法放在表一的 Mapper 中
    // 如果联合查询涉及多个业务模块，建议创建一个独立的 Mapper
    // 如果联合查询的业务逻辑更偏向于表二，可以放在表二的 Mapper 中，但需谨慎评估业务归属
    List<StudentSchoolClassDTO> selectStudentSchoolClass4(@Param("name") String name);

    // 分别查 2 表数据，在代码逻辑中进行联表
    // 依据 name 查 student
    List<Student> selectStudentByName(@Param("name") String name);

    /** ============== mapper xml 中 sql 语句的各种标签的使用 ============== **/
    // mapper xml 多种标签写法: <if> 标签条件判断
    // if 标签判断插入时候哪些字段需要插入到数据库中
    int insertStudentIfTag(Student student);

    // mapper xml 多种标签写法: <where> 标签条件判断
    // where 标签比如在查询时，能智能的处理 and 或者 or 关键字，并且当 where 标签中真的有条件语句时候，这个 where 关键字才会被灵活的生成否则不生成
    Student selectStudentWhereTag(@Param("name") String name, @Param("age") Integer age);

    // mapper xml 多种标签写法: <set> 标签
    // set 标签会职能的自动处理末尾多余的逗号
    int updateStudentSetTag(Student student);

    // mapper xml 多种标签写法: <choose>/<when>/<otherwise> 标签条件判断
    // 类似 java 中的 switch case 语句，choose 类似 switch，when 类似 case，otherwise 类似 default
    // <choose> 结构天生适合处理互斥条件。如果条件不是互斥的，使用多个 <if> 可能更合适
    Student selectStudentChooseTag(@Param("name") String name, @Param("age") Integer age);

    // mapper xml 多种标签写法: <foreach> 标签
    // 实现遍历，比如 in 某个 list
    Student selectStudentForeachTag(@Param("names") List<String> names);

    // mapper xml 多种标签写法: <trim> 标签
    // 它可以通过添加或移除内容的前缀、后缀，或者替换掉多余的关键字（如 AND、OR、逗号等）
    /*
    <trim
      prefix="前缀"
      suffix="后缀"
      prefixOverrides="要移除的前缀内容"
      suffixOverrides="要移除的后缀内容">
      <!-- 动态 SQL 内容 -->
    </trim>
    * */
    Student selectStudentTrimTag(@Param("name") String name);

    // mapper xml 多种标签写法: <bind> 标签
    // 用于在 OGNL 表达式上下文中创建一个变量，并将其绑定到当前上下文中，通常用于简化动态 SQL 中的复杂表达式、避免重复计算
    Student selectStudentBindTag(@Param("name") String name);

    // mapper xml 多种标签写法: <sql> 和 <include> 标签
    // 当多个 SQL 语句中存在重复的字段列表、表名、条件片段等时，<sql> 标签将这些重复的部分抽取为公共片段，<include> 标签引用 <sql> 标签定义的公共片段，将 SQL 片段动态插入到其他 SQL 语句中
    Student selectStudentIncludeTag(@Param("student_id") Long studentId);
}
