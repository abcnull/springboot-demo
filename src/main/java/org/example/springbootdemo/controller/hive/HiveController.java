package org.example.springbootdemo.controller.hive;

import com.alibaba.fastjson.JSONObject;
import org.example.springbootdemo.service.IHiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * hive controller
 * <p>
 * 直接在 Spring Boot 应用中频繁访问 Hive 通常不是最佳实践, 因为 Hive 设计初衷是 [面向批处理] 而非 [实时查询]
 * <p>
 * 【hive 为什么不要是实时查询/写入！】
 * hive 相比 mysql 查询速度较慢, 因为 hive 是基于 Map/Reduce 的, 而 mysql 是基于磁盘的，一个简单，
 * 假设一个简单查询，Hive 执行占用内存很大，cpu 时间很多，而 mysql 执行占用内存很小，cpu 时间也很少，
 * 所以如果 hive 来承接实时的大流量的查询，你的集群资源很容易耗尽，连接 hive 查询耗时久，连接池耗尽，新请求排队，服务 OOM，上游服务连锁出现故障，服务雪崩，应用很快卡死
 * <p>
 * 【业务开发如何存储到 hive】
 * 不能每次一个个写入 hive，每次写入都会造成产生 1/多个 HDFS 文件，长期这样下来，会产生太多小文件，然后 NameNode 就会崩溃，所以要成批成批的写入，减少写入频次
 * - 一般业务开发拿到业务数据后，代码中发往 kafka
 * - kafka 拿到消息后，然后编写 Spark Structured Streaming 的作业代码（scala）来消费 Kafka 数据，构成一定时间段成批的数据，再成批写入 hive
 * - Airflow 调度系统 + Shell 脚本来定时合并 hive 表中的小文件，优化存储和查询性能（因为前面即使一定时间成批写入 hive，其实还是有小文件问题，但不像实时写入那么严重，也还需要合并）
 * <p>
 * 【业务开发一般怎么查询 hive】
 * - hive 每天定时同步数据到 mysql
 * - 业务代码查询 mysql，但会有 T+1 延迟
 */
@RestController
@RequestMapping("/hive")
public class HiveController {
    @Autowired
    private IHiveService hiveService;

    // 写入 hive 表 click_event，实际是写入 kafka
    // 业务应用 → Kafka Topic → Spark Streaming作业 → HDFS (Hive表)
    @PostMapping("/write")
    public String writeData() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user_id", "123456");
        jsonObject.put("product_id", "123456");
        jsonObject.put("event_type", "click");
        jsonObject.put("event_time", System.currentTimeMillis());
        jsonObject.put("source", "web");

        hiveService.saveData(jsonObject);

        return "success";
    }

    // 从 hive 中读数据
    // 一定注意不要直接查询 hive，要通过定时同步到 mysql，去查询 mysql
    @GetMapping("/read")
    public String readData() {
        long time = System.currentTimeMillis();

        hiveService.queryData(time);

        return "success";
    }
}
