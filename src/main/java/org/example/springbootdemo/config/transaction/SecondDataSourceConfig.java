//package org.example.springbootdemo.config.transaction;
//
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.sql.DataSource;
//
///**
// * 第 2 个 mysql 数据源手动配置 config
// * 前提是 application.yml 要配置多个 mysql 数据源，你才需要配置 SecondDataSourceConfig
// */
//@Configuration
//@EnableTransactionManagement // 启用事务
//@MapperScan(basePackages = "org.example.springbootdemo.mapper2",
//        sqlSessionFactoryRef = "secondarySqlSessionFactory") // 指定该数据源扫描的 mapper 以及对应的 SqlSession 工厂
//public class SecondDataSourceConfig {
//    // 手动配置 DataSource Bean
//    // 数据源
//    @Bean(name = "secondaryDataSource")
//    @ConfigurationProperties(prefix = "spring.datasource.secondary")
//    public DataSource secondaryDataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    // 手动配置 SqlSessionFactory bean
//    // 生成 SqlSession
//    @Bean(name = "secondarySqlSessionFactory")
//    public SqlSessionFactory secondarySqlSessionFactory(@Qualifier("secondaryDataSource") DataSource dataSource) throws Exception {
//        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
//        sessionFactoryBean.setDataSource(dataSource);
//        return sessionFactoryBean.getObject();
//    }
//
//    // 手动配置 DataSourceTransactionManager bean
//    // 事务管理器
//    @Bean(name = "secondaryTransactionManager")
//    public DataSourceTransactionManager secondaryTransactionManager(@Qualifier("secondaryDataSource") DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }
//}
//
//
