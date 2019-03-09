package ru.bookshop.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.bookshop.ParameterNames;
import ru.bookshop.entity.Book;
import ru.bookshop.exception.ApplicationException;
import ru.bookshop.service.BookService;
import ru.bookshop.service.impl.BookServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@WebServlet (urlPatterns = Paths.BOOKS + "/*")
public class BookController extends BaseController {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final Map<Pattern, BaseController.RequestHandler> getHandlers = new HashMap<>();
    private final Map<Pattern, BaseController.RequestHandler> postHandlers = new HashMap<>();
    private final Map<Pattern, BaseController.RequestHandler> putHandlers = new HashMap<>();

    private final BookService bookService = BookServiceImpl.getInstance();

    public BookController() {
        getHandlers.put(Pattern.compile("^(/books)/?$"), this::get);
        getHandlers.put(Pattern.compile("^(/books/)([0-9]+)$"), this::getById);

        postHandlers.put(Pattern.compile("^(/books)/?$"), this::add);

        putHandlers.put(Pattern.compile("^(/books/)([0-9]+)$"), this::update);

        putHandlers.put(Pattern.compile("^(/books/)([0-9]+)$"), this::remove);
    }

    private void get(List<String> urlParts, HttpServletRequest req, HttpServletResponse resp) throws ApplicationException, IOException {
        String name = req.getParameter(ParameterNames.NAME);
        String authorName = req.getParameter(ParameterNames.AUTHOR_NAME);

        List<Book> books = bookService.get(name, authorName);

        String response = OBJECT_MAPPER.writeValueAsString(books);

        resp.getWriter().write(response);
    }

    private void getById(List<String> urlParts, HttpServletRequest req, HttpServletResponse resp) throws ApplicationException, IOException {
        long id = ParameterParser.parseLongInUrl("Book", urlParts.get(1));

        Book book = bookService.getById(id);

        String response = OBJECT_MAPPER.writeValueAsString(book);

        resp.getWriter().write(response);
    }

    private void add(List<String> urlParts, HttpServletRequest req, HttpServletResponse resp) throws ApplicationException, IOException {
        Map<String, String> params = parseBody(req);
        String name = params.get(ParameterNames.NAME);
        String description = params.get(ParameterNames.DESCRIPTION);
        Long authorId = ParameterParser.parseLong(ParameterNames.AUTHOR_ID, params.get(ParameterNames.AUTHOR_ID));

        Book book = bookService.add(name, description, authorId);

        String response = OBJECT_MAPPER.writeValueAsString(book);

        resp.getWriter().write(response);
    }

    private void update(List<String> urlParts, HttpServletRequest req, HttpServletResponse resp) throws ApplicationException, IOException {
        long id = ParameterParser.parseLongInUrl("Book", urlParts.get(1));

        Map<String, String> params = parseBody(req);
        String name = params.get(ParameterNames.NAME);
        String description = params.get(ParameterNames.DESCRIPTION);
        Long authorId = ParameterParser.parseLong(ParameterNames.AUTHOR_ID, params.get(ParameterNames.AUTHOR_ID));

        Book book = bookService.update(id, name, description, authorId);

        String response = OBJECT_MAPPER.writeValueAsString(book);

        resp.getWriter().write(response);
    }

    private void remove(List<String> urlParts, HttpServletRequest req, HttpServletResponse resp) throws ApplicationException {
        long id = ParameterParser.parseLongInUrl("Book", urlParts.get(1));

        bookService.remove(id);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doRequest(getHandlers, req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doRequest(postHandlers, req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doRequest(putHandlers, req, resp);
    }
}
