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
//import org.springframework.context.annotation.Primary;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.sql.DataSource;
//
///**
// * 第 1 个(首要) mysql 数据源手动配置 config
// */
//@Configuration
//@EnableTransactionManagement // 启用事务
//@MapperScan(basePackages = "org.example.springbootdemo.mapper",
//        sqlSessionFactoryRef = "primarySqlSessionFactory") // 指定该数据源扫描的 mapper 以及对应的 SqlSession 工厂
//public class PrimaryDataSourceConfig {
//    // 手动配置 DataSource Bean
//    // 数据源
//    @Primary
//    @Bean(name = "primaryDataSource")
//    @ConfigurationProperties(prefix = "spring.datasource.primary")
//    public DataSource primaryDataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    // 手动配置 SqlSessionFactory bean
//    // 生成 SqlSession
//    @Primary
//    @Bean(name = "primarySqlSessionFactory")
//    public SqlSessionFactory primarySqlSessionFactory(@Qualifier("primaryDataSource") DataSource dataSource) throws Exception {
//        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
//        sessionFactoryBean.setDataSource(dataSource);
//        return sessionFactoryBean.getObject();
//    }
//
//    // 手动配置 DataSourceTransactionManager bean
//    // 事务管理器
//    @Primary
//    @Bean(name = "primaryTransactionManager")
//    public DataSourceTransactionManager primaryTransactionManager(@Qualifier("primaryDataSource") DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }
//}
