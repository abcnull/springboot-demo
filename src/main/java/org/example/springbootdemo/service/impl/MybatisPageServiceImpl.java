package org.example.springbootdemo.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.springbootdemo.mapper.StudentMapper;
import org.example.springbootdemo.model.Student;
import org.example.springbootdemo.service.IMybatisPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MybatisPageServiceImpl implements IMybatisPageService {
    @Autowired
    private StudentMapper studentMapper;

    // 使用 PageHelper
    // 分页查询学生信息，依据学生姓名查询
    // 标准 pageHelper 使用，开启分页后，查询操作必须要紧跟 startPage 代码
    public List<Student> searchStudentByPage(String name, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        // 需要紧紧跟着上一行代码
        // 查出来的是 满足条件的 已经分页后的数据
        List<Student> students = studentMapper.selectStudentByPage(name);

        // 获取分页信息
        PageInfo<Student> pageInfo = new PageInfo<>(students);
        long total = pageInfo.getTotal(); // 总数
        System.out.println("满足条件的全量数据数量是：" + total);
        // 获取当页数据
        return pageInfo.getList();
    }

    // 使用 PageHelper
    // 分页查询学生信息，依据学生姓名查询
    // 使用 pagehelper lambda 表达式写法
    public List<Student> searchStudentByPage2(String name, int pageNum, int pageSize) {
        // 分页设置 + 查询，流式处理
        PageInfo<Student> pageInfo = PageHelper.startPage(pageNum, pageSize)
                .doSelectPageInfo(() -> studentMapper.selectStudentByPage(name));

        // 获取分页的信息
        long total = pageInfo.getTotal(); // 总数
        System.out.println("满足条件的全量数据数量是：" + total);
        // 获取分页数据
        return pageInfo.getList();
    }

    // 不使用 PageHelper
    // 分页查询学生信息，依据学生姓名查询
    // 直接通过在 sql 中拼接 limit 相关语句来实现
    public List<Student> searchStudentByPage3(String name, int pageNum, int pageSize) {
        // limit 中 offset 是从 0 开始计算，如果过滤 1000 条，则 offset 填 1000，表示过滤前 1000 条，即从 index = 1000 开始
        int offset = (pageNum - 1) * pageSize;
        return studentMapper.selectStudentWithLimit(name, offset, pageSize);
    }

    // 不使用 PageHelper
    // 分页查询学生信息，依据学生姓名查询
    // 直接在内存中进行逻辑遍历找到指定页码的数据
    public List<Student> searchStudentByPage4(String name, int pageNum, int pageSize) {
        List<Student> students = studentMapper.selectStudentByPage(name);
        int offset = (pageNum - 1) * pageSize;
        if (offset >= students.size()) {
            return null;
        }

        // 内存分页，可能会导致内存爆炸，所以建议用 pagehelper 分页，或者 limit 语句直接拼接
        // [from, to)
        return students.subList(offset, pageNum * pageSize);
    }
}
