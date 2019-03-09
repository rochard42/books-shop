package ru.bookshop.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import ru.bookshop.exception.ApplicationException;
import ru.bookshop.exception.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class BaseController extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(BaseController.class);

    private static final ObjectWriter OBJECT_WRITER = new ObjectMapper().writerFor(Error.class);

    void doRequest(
            Map<Pattern, RequestHandler> handlers,
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws IOException {
        boolean requestHandled = false;
        for (Map.Entry<Pattern, RequestHandler> entry : handlers.entrySet()) {
            Pattern urlPattern = entry.getKey();
            Matcher matcher = urlPattern.matcher(req.getRequestURI());
            if (matcher.matches()) {
                RequestHandler handler = entry.getValue();
                List<String> urlParts = new ArrayList<>(matcher.groupCount());
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    urlParts.add(matcher.group(i));
                }

                try {
                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");

                    handler.handle(urlParts, req, resp);
                } catch (ApplicationException e) {
                    log.error("Application ru.bookshop.exception occurred: ", e);
                    sendError(e, resp);
                }
                requestHandled = true;
                break;
            }
        }

        if (!requestHandled) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void sendError(ApplicationException e, HttpServletResponse resp) throws IOException {
        Error error = new Error(e.getCode().getValue(), e.getMessage());
        resp.setStatus(getHttpCode(e.getCode()));
        resp.getWriter().write(OBJECT_WRITER.writeValueAsString(error));
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

    Map<String, String> parseBody(HttpServletRequest req) throws IOException {
        Map<String, String> result = new HashMap<>();

        String body = req.getReader().readLine();
        if (body == null || body.isEmpty()) {
            return result;
        }

        String[] params = body.split("&");
        for (String param : params) {
            if (param != null && !param.isEmpty()) {
                String[] paramParts = param.split("=");
                if (paramParts.length == 2) {
                    result.put(paramParts[0], paramParts[1]);
                }
            }
        }

        return result;
    }

    @FunctionalInterface
    public interface RequestHandler {
        void handle(List<String> urlParts, HttpServletRequest req, HttpServletResponse resp) throws ApplicationException, IOException;
    }

}
