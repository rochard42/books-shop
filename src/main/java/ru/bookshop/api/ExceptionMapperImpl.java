package ru.bookshop.api;

import ru.bookshop.exception.ApplicationException;
import ru.bookshop.exception.ErrorCode;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionMapperImpl implements ExceptionMapper<Exception> {

    private static final int DEFAULT_ERROR_CODE = 1;
    private static final String DEFAULT_ERROR_MESSAGE = "internal server error";

    @Override
    public Response toResponse(Exception e) {
        if (e instanceof ApplicationException) {
            ApplicationException exception = (ApplicationException) e;
            Error error = new Error(exception.getCode().getValue(), e.getMessage());
            return Response.status(getHttpCode(exception.getCode()))
                    .entity(error)
                    .build();
        } else if (e instanceof WebApplicationException) {
            WebApplicationException webApplicationException = (WebApplicationException) e;
            return webApplicationException.getResponse();
        } else {
            Error error = new Error(DEFAULT_ERROR_CODE, DEFAULT_ERROR_MESSAGE);
            return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }

    private int getHttpCode(ErrorCode code) {
        switch (code) {
            case INTERNAL_ERROR:
                return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            case INVALID_ARGUMENT:
            case USER_NAME_ALREADY_IN_USE:
            case LOGIN_OR_PASSWORD_INVALID:
                return HttpServletResponse.SC_BAD_REQUEST;
            case SERVICE_UNAVAILABLE:
                return HttpServletResponse.SC_SERVICE_UNAVAILABLE;
            case OBJECT_NOT_FOUND:
                return HttpServletResponse.SC_NOT_FOUND;
            case ACCESS_DENIED:
                return HttpServletResponse.SC_UNAUTHORIZED;
            default:
                return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        }
    }
}
