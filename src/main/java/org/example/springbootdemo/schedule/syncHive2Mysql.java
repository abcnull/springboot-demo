package org.example.springbootdemo.schedule;

import lombok.extern.slf4j.Slf4j;
import org.example.springbootdemo.mapper.ClickDataMapper;
import org.example.springbootdemo.model.ClickData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 每天凌晨定时同步 hive 中数据到 mysql schedule
 * 一般公司可能有对对应的定时任务 task 服务专门来编写定时任务脚本，其中编写代码来实现从 hive 读去数据，以及把数据写入 mysql
 */
@Component
@Slf4j
public class syncHive2Mysql {
    // 操作 mysql
    @Autowired
    private ClickDataMapper clickDataMapper;

    // 读取 hive
    @Autowired
    private JdbcTemplate hiveTemplate;

    // 简单的 RowMapper，映射 Hive 查询结果到 对象
    private static final RowMapper<ClickData> CLICK_ROW_MAPPER = (rs, rowNum) -> {
        ClickData clickData = new ClickData();
        clickData.setUserId(rs.getString("user_id"));
        clickData.setProductId(rs.getString("product_id"));
        clickData.setEventType(rs.getString("event_type"));
        clickData.setEventTime(rs.getLong("event_time"));
        clickData.setSource(rs.getString("source"));
        return clickData;
    };

    /**
     * 每天定时执行
     */
    @Scheduled(cron = "0 0 2 * * ?") // 每天凌晨2点
    public void syncData() {
        // 1. 从Hive获取数据
        String sql = ""; // 自己写 sql
        List<ClickData> clickDataList = hiveTemplate.query(sql, CLICK_ROW_MAPPER);

        // 事务操作，从 hive 写入 mysql
        try {
            handle(clickDataList);
        } catch (Exception e) {
            log.error("syncData, 同步 hive 数据到 mysql 失败", e);
        }
    }

    // mysql 事务操作
    @Transactional
    public void handle(List<ClickData> clickDataList) {
        // 2.看是否清除 mysql 指定条件数据
        System.out.println("看实际业务需不需要清除 mysql 中相应重复或者错误数据");

        // 3. mysql 插入新数据
        for (ClickData clickData : clickDataList) {
            clickDataMapper.insertData(clickData);
        }
    }
}
