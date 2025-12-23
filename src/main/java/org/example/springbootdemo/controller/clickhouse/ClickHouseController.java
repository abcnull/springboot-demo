package org.example.springbootdemo.controller.clickhouse;

import com.alibaba.fastjson.JSON;
import org.example.springbootdemo.service.IClickHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * clickhouse controller
 * <p>
 * ClickHouse 在 日志分析、实时报表、行为分析 中非常常用。
 * 一般项目中只做查询操作，写入操作通过 ETL 将比如 hive 中数据同步到 clickhouse，ETL 一般公司有专门的平台提供，同步因为 hive 查询非常慢，且一般都不会在项目中查询，clickhouse 数据查询很快，适合在业务代码中进行查询
 * <p>
 * 这里展示非常常用 jdbc 连接 clickhouse 查询数据的方式
 */
@RestController
@RequestMapping("/clickhouse")
public class ClickHouseController {
    @Autowired
    private IClickHouseService clickHouseService;

    /**
     * clickhouse 来查询，其中其实依赖于 JDBC 连接
     */
    @GetMapping("/query")
    public String query() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);
        String eventType = "click";

        System.out.println(JSON.toJSONString(
                clickHouseService.getUserBehaviorStats(startDate, endDate, eventType)
        ));

        return "success";
    }
}
