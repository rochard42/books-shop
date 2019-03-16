package ru.bookshop.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("ru.bookshop.service")
@ComponentScan("ru.bookshop.repository")
@PropertySource("classpath://application.properties")
public class AppConfig {
}

