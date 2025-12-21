package org.example.springbootdemo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import org.elasticsearch.core.Map;
import org.example.springbootdemo.mapper.ClickDataMapper;
import org.example.springbootdemo.model.ClickData;
import org.example.springbootdemo.service.IHiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * hive service
 */
@Service
public class HiveServiceImpl implements IHiveService {
    // 这个 kafka 已经在 application.yml 中配置好了
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private ClickDataMapper clickDataMapper;

    /**
     * 点击事件需要存储到 hive 中，一定不能直接写入 hive，要通过发往 kafka
     * 我这里只演示了发往 kafka，其实后续还有 spark 来处理 kafka 数据，以及 Airflow 合并小文件
     * <p>
     * 业务应用 → Kafka Topic → Spark Streaming作业 → HDFS (Hive表)
     * (Java)    (消息队列)      (Scala/Java程序)     (分布式存储)
     * <p>
     * 【业务开发如何存储到 hive】
     * 不能每次一个个写入 hive，每次写入都会造成产生 1/多个 HDFS 文件，长期这样下来，会产生太多小文件，然后 NameNode 就会崩溃，所以要成批成批的写入，减少写入频次
     * - 一般业务开发拿到业务数据后，代码中发往 kafka
     * - kafka 拿到消息后，然后编写 Spark Structured Streaming 的作业代码（scala）来消费 Kafka 数据，构成一定时间段成批的数据，再成批写入 hive
     * - Airflow 调度系统 + Shell 脚本来定时合并 hive 表中的小文件，优化存储和查询性能（因为前面即使一定时间成批写入 hive，其实还是有小文件问题，但不像实时写入那么严重，也还需要合并）
     */
    @Override
    public void saveData(JSONObject clickData) {
        String topic = "my-topic";
        String msg = JSON.toJSONString(Map.of(
                "user_id", clickData.getString("user_id"),
                "product_id", clickData.getString("product_id"),
                "event_type", clickData.getString("event_type"),
                "event_time", System.currentTimeMillis(),
                "source", "app"));

        // 发往 kafka
        kafkaTemplate.send(topic, msg);
    }


    /**
     * 从 hive 中查询数据，一定注意不要直接查询 hive，要通过定时同步到 mysql，去查询 mysql
     * <p>
     * 【业务开发一般怎么查询 hive】
     * - hive 每天定时同步数据到 mysql
     * - 业务代码查询 mysql，但会有 T+1 延迟
     */
    @Override
    public ClickData queryData(long timestamp) {
        // syncHive2Mysql：首先其实每天凌晨有定时任务在跑，在把 hive 中指定条件的数据同步到 mysql 中
        System.out.println("syncHive2Mysql：凌晨已经有数据从 hive 中同步到了 mysql 了");

        // 看自己业务要不要对 mysql 中数据进行删除，来提出重复或者错误数据
        System.out.println("这里看业务，是否要对 mysql 中数据做剔除来删除重复或者错误数据");

        // 然后就可以从 mysql 中查询数据了
        return clickDataMapper.queryData(timestamp);
    }
}
