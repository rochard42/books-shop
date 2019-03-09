package ru.bookshop.service.impl;

import ru.bookshop.exception.ApplicationException;
import ru.bookshop.exception.ErrorCode;

final class Validator {

    static void checkNotNull(String parameterName, Object value) throws ApplicationException {
        if (value == null) {
            throw new ApplicationException(
                    ErrorCode.INVALID_ARGUMENT,
                    String.format("Parameter '%s' must be not null", parameterName)
            );
        }
    }

    private Validator() {
    }
}
