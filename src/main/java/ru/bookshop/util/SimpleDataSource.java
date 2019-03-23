//package ru.bookshop.util;
//
//import org.postgresql.Driver;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.sql.DataSource;
//import java.io.PrintWriter;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.sql.SQLFeatureNotSupportedException;
//
//
//public class SimpleDataSource implements DataSource {
//
//    private static final Logger log = LoggerFactory.getLogger(SimpleDataSource.class);
//
//    private String url;
//    private String username;
//    private String password;
//
//    public SimpleDataSource(String url, String username, String password) {
//        this.url = url;
//        this.username = username;
//        this.password = password;
//    }
//
//    public Connection getConnection() throws SQLException {
//        return DriverManager.getConnection(url, username, password);
//    }
//
//    @Override
//    public Connection getConnection(String username, String password) throws SQLException {
//        return DriverManager.getConnection(url, username, password);
//    }
//
//    static {
//        try {
//            DriverManager.registerDriver(new Driver());
//        } catch (SQLException e) {
//            log.error("Unable to register JDBC driver");
//        }
//    }
//
//    @Override
//    public <T> T unwrap(Class<T> iface) throws SQLException {
//        return null;
//    }
//
//    @Override
//    public boolean isWrapperFor(Class<?> iface) throws SQLException {
//        return false;
//    }
//
//    @Override
//    public PrintWriter getLogWriter() throws SQLException {
//        return null;
//    }
//
//    @Override
//    public void setLogWriter(PrintWriter out) throws SQLException {
//
//    }
//
//    @Override
//    public void setLoginTimeout(int seconds) throws SQLException {
//
//    }
//
//    @Override
//    public int getLoginTimeout() throws SQLException {
//        return 0;
//    }
//
//    @Override
//    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
//        return null;
//    }
//}
//
//
