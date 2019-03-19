package ru.bookshop.configuration;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("ru.bookshop.service")
@ComponentScan("ru.bookshop.repository")
@PropertySource("classpath://application.properties")
public class AppConfig {

    @Value("${db.driverClassName}")
    String driver;

    @Value("${db.url}")
    String url;
    @Value("${db.username}")
    String username;
    @Value("${db.password}")
    String password;

    @Bean("basicDataSource")
    BasicDataSource dataSource() {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName(driver);
        basicDataSource.setUrl(url);
        basicDataSource.setUsername(username);
        basicDataSource.setPassword(password);

        return basicDataSource;
    }
}