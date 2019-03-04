package repository.impl;

import exception.ApplicationException;
import exception.ErrorCode;
import org.postgresql.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;

class JdbcUtils {

    private static final Logger log = LoggerFactory.getLogger(JdbcUtils.class);

    private static final String URL = "jdbc:postgresql://stampy.db.elephantsql.com:5432/zoreovlw";
    private static final String USER_NAME = "zoreovlw";
    private static final String PASSWORD = "E-E6PE_pnorOdZyEXEoFnNFeP7sFqqso";

    static {
        try {
            DriverManager.registerDriver(new Driver());
        } catch (SQLException e) {
            log.error("Unable to register JDBC driver");
        }
    }

    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER_NAME, PASSWORD);
    }

    static ApplicationException translateSQLException(SQLException e) {
        Throwable rootCause = getRootException(e);

        if (rootCause instanceof IOException) {
            return new ApplicationException(
                    ErrorCode.SERVICE_UNAVAILABLE,
                    "Database unavailable",
                    e
            );
        } else {
            return new ApplicationException(
                    ErrorCode.INTERNAL_ERROR,
                    "Database interaction error",
                    e
            );
        }
    }

    private static Throwable getRootException(Throwable t) {
        while (t.getCause() != null) {
            t = t.getCause();
        }
        return t;
    }

    private static void closeResultSetQuietly(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException ignore) {/*NOP*/}
    }

    private static void closeStatementQuietly(Statement st) {
        try {
            if (st != null) {
                st.close();
            }
        } catch (SQLException ignore) {/*NOP*/}
    }

    private static void closeConnectionQuietly(Connection con) {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException ignore) {/*NOP*/}
    }

    static void closeResultSetQuietly(ResultSet rs, Statement st, Connection con) {
        closeResultSetQuietly(rs);
        closeStatementQuietly(st);
        closeConnectionQuietly(con);
    }

    static void closeStatementQuietly(Statement st, Connection con) {
        closeStatementQuietly(st);
        closeConnectionQuietly(con);
    }
}
