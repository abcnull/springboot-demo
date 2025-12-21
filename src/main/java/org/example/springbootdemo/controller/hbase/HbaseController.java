package org.example.springbootdemo.controller.hbase;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * hbase controller
 * <p>
 * hbase 和 hive 类似都是基于 HDFS 存储的，都是属于 hadoop 生态中的，但是 hive 是离线表，不可实时查询，而 hbase 可以实时去查询
 * 二者都可以用来存储诸如用户行为数据，点击事件等
 * <p>
 * 那为啥不把数据都给 hbase 存储呢？
 * 虽然 hbase 看似很强大，但是用 hbase 存储 hive 的数据就像跑车去拉快递，因为 HBase 每 TB 数据需要 3-4 台服务器，而 hive 只需要 1 台
 * 比如某 10TB 用户行为数据，存储到 hbase 估计要 40 台高性能服务器，每月花费 21w/月，而如果最近 7 天热点数据存储到 hbase 中，非热点数据存储到 hive 中，估计成本只有 7w/月，成本大幅降低
 * 而且 hbase 实时查询要注意如果扫描大量数据比如亿级行数可能会导致 hbase 集群崩掉，而 hive 查询时候扫描亿级行数一般不会有问题
 * <p>
 * HBase 处理"现在发生了什么"，Hive 分析"过去发生了什么"
 * <p>
 * 适合在HBase上执行SQL的场景：
 * - 简单点查：SELECT * FROM table WHERE rowkey = 'xxx'
 * - 有限范围扫描：SELECT * FROM table WHERE rowkey LIKE 'user123_%' LIMIT 1000
 * - 计数器查询：SELECT total FROM counters WHERE date = '2024-01-15'
 * - 时间窗口查询：SELECT * FROM events WHERE rowkey BETWEEN '20240115' AND '20240116'
 * 绝对不要在HBase上执行的SQL：
 * - 全表聚合：SELECT COUNT(*), AVG(value) FROM huge_table
 * - 多表JOIN：SELECT u.*, o.* FROM users u JOIN orders o ON u.id = o.user_id
 * - 模糊查询：SELECT * FROM table WHERE name LIKE '%john%'
 * - 大偏移分页：SELECT * FROM table ORDER BY time LIMIT 10 OFFSET 1000000
 */
@RestController
@RequestMapping("/hbase")
public class HbaseController {

}
