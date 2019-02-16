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

    static void closeResultSet(ResultSet rs) {
        Statement st = null;
        try {
            st = rs.getStatement();
        } catch (SQLException ignore) {/*NOP*/}

        try {
            rs.close();
        } catch (SQLException ignore) {/*NOP*/}

        closeStatement(st);
    }

    static void closeStatement(Statement st) {
        Connection con = null;
        try {
            con = st != null ? st.getConnection() : null;
        } catch (SQLException ignore) {/*NOP*/}

        try {
            if (st != null) {
                st.close();
            }
        } catch (SQLException ignore) {/*NOP*/}

        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException ignore) {/*NOP*/}
    }

}
