package ru.bookshop.api;

import ru.bookshop.exception.ApplicationException;
import ru.bookshop.exception.ErrorCode;

final class ParameterParser {

    static Long parseLong(String parameterName, String value) throws ApplicationException {
        if (value == null) {
            return null;
        }

        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            throw new ApplicationException(
                    ErrorCode.INVALID_ARGUMENT,
                    String.format("Parameter '%s' must be a number (invalid value '%s')", parameterName, value),
                    e
            );
        }
    }

    static long parseLongInUrl(String parameterName, String value) throws ApplicationException {
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            throw new ApplicationException(
                    ErrorCode.OBJECT_NOT_FOUND,
                    String.format("'%s' with id %s not found", parameterName, value),
                    e
            );
        }
    }

    private ParameterParser() {
    }
}
