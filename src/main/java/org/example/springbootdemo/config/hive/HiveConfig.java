//package org.example.springbootdemo.config.hive;
//
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.core.JdbcTemplate;
//
///**
// * hive config
// * <p>
// * hive 数据源配置
// */
//@Configuration
//public class HiveConfig {
//    @Value("${spring.hive.url}")
//    private String url;
//
//    @Value("${spring.hive.username}")
//    private String username;
//
//    @Value("${spring.hive.password}")
//    private String password;
//
//    @Value("${spring.hive.driver-class-name}")
//    private String driverClassName;
//
//    // Spring Boot 2.x 默认 HikariCP 数据源，但 Hive 兼容性稍差
//    @Bean
//    public HikariDataSource hiveDataSource() {
//        HikariConfig config = new HikariConfig();
//        config.setJdbcUrl(url);
//        config.setUsername(username);
//        config.setPassword(password);
//        config.setDriverClassName(driverClassName);
//
//        // Hikari 特有配置
//        config.setMaximumPoolSize(5);
//        config.setMinimumIdle(2);
//        config.setConnectionTimeout(300000); // 5分钟
//        config.setIdleTimeout(600000); // 10分钟
//        config.setMaxLifetime(1800000); // 30分钟
//
//        // 关键：禁用自动提交（Hive 不支持）
//        config.setAutoCommit(false);
//
//        // 关键：Hive 兼容性配置
//        config.addDataSourceProperty("dataSource.useSSL", "false");
//        config.addDataSourceProperty("dataSource.allowLoadLocalInfile", "true");
//
//        return new HikariDataSource(config);
//    }
//
//    // 返回可操作 hive 数据库的 jdbcTemplate
//    @Bean
//    public JdbcTemplate hiveJdbcTemplate() {
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(hiveDataSource());
//        jdbcTemplate.setQueryTimeout(3600); // 1小时查询超时
//        return jdbcTemplate;
//    }
//}
//
//// ⬆️ 当 hive 数据库存在时，需要配置 hive 数据源，再把这块开放
