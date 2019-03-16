package ru.bookshop.repository.impl;

import org.postgresql.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class DataSource {

    private static final Logger log = LoggerFactory.getLogger(DataSource.class);

    @Value("${jdbc.url}")
    private String url;

    @Value("${jdbc.username}")
    private String username;

    @Value("${jdbc.password}")
    private String password;

    Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    static {
        try {
            DriverManager.registerDriver(new Driver());
        } catch (SQLException e) {
            log.error("Unable to register JDBC driver");
        }
    }
}


