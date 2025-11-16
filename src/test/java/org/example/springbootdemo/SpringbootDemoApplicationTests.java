package org.example.springbootdemo;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.springbootdemo.service.IBasisMybatisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class SpringbootDemoApplicationTests {

    @Autowired
    private IBasisMybatisService basisMybatisService;

    @Test
    void contextLoads() {
        log.debug("ddddddd");
        System.out.println("aaaaa");
    }

    @Test
    public void func_7_29() {
        System.out.println(JSON.toJSONString(basisMybatisService.searchStudentById(1L)));
    }

}
