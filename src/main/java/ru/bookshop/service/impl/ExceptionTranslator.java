package ru.bookshop.service.impl;

import ru.bookshop.exception.ApplicationException;
import ru.bookshop.exception.ErrorCode;

import java.io.IOException;

class ExceptionTranslator {

    static ApplicationException translateDataAccessException(Exception e) {
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

}
