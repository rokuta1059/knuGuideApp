package com.kangwonapp.kangwonapi.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        String username = "admin";
        String password = "knuappdb";
        String jdbcUrl = "jdbc:mysql://kangwonapi-db.cfmp0bi6d6ix.ap-northeast-2.rds.amazonaws.com:3306/knuapp?serverTimezone=UTC&characterEncoding=UTF-8&allowPublicKeyRetrieval=true&useSSL=false";
        String driverClass = "com.mysql.cj.jdbc.Driver";

        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.username(username);
        dataSourceBuilder.password(password);
        dataSourceBuilder.url(jdbcUrl);
        dataSourceBuilder.driverClassName(driverClass);
        return dataSourceBuilder.build();
    }
}
