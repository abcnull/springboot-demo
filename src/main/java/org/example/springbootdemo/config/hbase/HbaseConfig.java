//package org.example.springbootdemo.config.hbase;
//
//import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.hbase.HBaseConfiguration;
//import org.apache.hadoop.hbase.client.Connection;
//import org.apache.hadoop.hbase.client.ConnectionFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//
//import java.io.IOException;
//
///**
// * Hbase config
// */
//@org.springframework.context.annotation.Configuration
//public class HbaseConfig {
//
//    @Value("${spring.hbase.zookeeper.quorum}")
//    private String zookeeperQuorum;
//
//    @Value("${spring.hbase.zookeeper.property.clientPort}")
//    private String clientPort;
//
//    @Value("${spring.hbase.znode.parent}")
//    private String znodeParent;
//
//    // 设置配置
//    @Bean
//    public Configuration getHBaseConfiguration() {
//        Configuration configuration = HBaseConfiguration.create();
//        configuration.set("hbase.zookeeper.quorum", zookeeperQuorum);
//        configuration.set("hbase.zookeeper.property.clientPort", clientPort);
//        configuration.set("zookeeper.znode.parent", znodeParent); // 通常是/hbase
//        // 如有需要，设置其他参数
//        // configuration.set("hbase.rpc.timeout", "60000");
//        // configuration.set("hbase.client.operation.timeout", "120000");
//        return configuration;
//    }
//
//    // 创建 Hbase 连接
//    @Bean
//    public Connection hbaseConnection(Configuration configuration) throws IOException {
//        return ConnectionFactory.createConnection(configuration);
//    }
//}
