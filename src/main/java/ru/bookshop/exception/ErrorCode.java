package ru.bookshop.exception;

public enum ErrorCode {

    INTERNAL_ERROR(1),
    INVALID_ARGUMENT(2),
    SERVICE_UNAVAILABLE(3),
    OBJECT_NOT_FOUND(4),
    USER_NAME_ALREADY_IN_USE(5),
    LOGIN_OR_PASSWORD_INVALID(6),
    ACCESS_DENIED(7);

    private final int value;

    ErrorCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
