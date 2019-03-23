package ru.bookshop.repository.impl;

import ru.bookshop.exception.ApplicationException;
import ru.bookshop.exception.ErrorCode;
import org.postgresql.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;

class JdbcUtils {

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

    private static void closeQuietly(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException ignore) {/*NOP*/}
    }

    private static void closeQuietly(Statement st) {
        try {
            if (st != null) {
                st.close();
            }
        } catch (SQLException ignore) {/*NOP*/}
    }

    private static void closeQuietly(Connection con) {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException ignore) {/*NOP*/}
    }

    static void closeQuietly(ResultSet rs, Statement st, Connection con) {
        closeQuietly(rs);
        closeQuietly(st);
        closeQuietly(con);
    }

    static void closeQuietly(Statement st, Connection con) {
        closeQuietly(st);
        closeQuietly(con);
    }
}
