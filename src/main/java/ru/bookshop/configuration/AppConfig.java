package ru.bookshop.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("ru.bookshop.service")
@ComponentScan("ru.bookshop.repository")
public class AppConfig {
}
