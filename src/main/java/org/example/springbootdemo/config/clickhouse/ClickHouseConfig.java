package org.example.springbootdemo.config.clickhouse;

import com.clickhouse.jdbc.ClickHouseDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * clickhouse config
 * <p>
 * clickhouse 配置连接，产生 JdbcTemplate 来操作 clickhouse
 */
@Configuration
public class ClickHouseConfig {
    @Value("${spring.clickhouse.url}")
    private String url;

    @Bean
    public DataSource clickHouseDataSource() throws SQLException {
        // 直接在配置文件中包含用户名和密码（最简洁的方式）
        // application.yml 中配置: jdbc:clickhouse://host:8123/database?user=username&password=password
        return new ClickHouseDataSource(url);
    }

    @Bean
    public JdbcTemplate clickHouseJdbcTemplate(DataSource clickHouseDataSource) {
        return new JdbcTemplate(clickHouseDataSource);
    }
}
