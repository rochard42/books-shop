package api;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Book;
import exception.ApplicationException;
import exception.ErrorCode;
import service.BookService;
import service.impl.BookServiceImpl;

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
        String name = req.getParameter("name");
        String authorName = req.getParameter("authorName");

        List<Book> books = bookService.get(name, authorName);

        String response = OBJECT_MAPPER.writeValueAsString(books);

        resp.getWriter().write(response);
    }

    private void getById(List<String> urlParts, HttpServletRequest req, HttpServletResponse resp) throws ApplicationException, IOException {
        Long id;
        try {
            id = Long.parseLong(urlParts.get(1));
        } catch (Exception e) {
            throw new ApplicationException(
                    ErrorCode.OBJECT_NOT_FOUND,
                    String.format("Book with id %s not found", urlParts.get(1)),
                    e
            );
        }

        Book book = bookService.getById(id);

        String response = OBJECT_MAPPER.writeValueAsString(book);

        resp.getWriter().write(response);
    }

    private void add(List<String> urlParts, HttpServletRequest req, HttpServletResponse resp) throws ApplicationException, IOException {
        Map<String, String> params = parseBody(req);
        String name = params.get("name");
        String description = params.get("description");
        Long authorId;
        try {
            authorId = Long.parseLong(params.get("authorId"));
        } catch (Exception e) {
            throw new ApplicationException(
                    ErrorCode.INVALID_ARGUMENT,
                    String.format("%s is not valid author id", params.get("authorId")),
                    e
            );
        }

        Book book = bookService.add(name, description, authorId);

        String response = OBJECT_MAPPER.writeValueAsString(book);

        resp.getWriter().write(response);
    }

    private void update(List<String> urlParts, HttpServletRequest req, HttpServletResponse resp) throws ApplicationException, IOException {
        Long id;
        try {
            id = Long.parseLong(urlParts.get(1));
        } catch (Exception e) {
            throw new ApplicationException(
                    ErrorCode.OBJECT_NOT_FOUND,
                    String.format("Book with id %s not found", urlParts.get(1)),
                    e
            );
        }

        Map<String, String> params = parseBody(req);
        String name = params.get("name");
        String description = params.get("description");
        Long authorId = Long.parseLong(params.get("authorId"));

        Book book = bookService.update(id, name, description, authorId);

        String response = OBJECT_MAPPER.writeValueAsString(book);

        resp.getWriter().write(response);
    }

    private void remove(List<String> urlParts, HttpServletRequest req, HttpServletResponse resp) throws ApplicationException, IOException {
        Long id;
        try {
            id = Long.parseLong(urlParts.get(1));
        } catch (Exception e) {
            throw new ApplicationException(
                    ErrorCode.OBJECT_NOT_FOUND,
                    String.format("Book with id %s not found", urlParts.get(1)),
                    e
            );
        }

        bookService.remove(id); // :D
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doRequest(getHandlers, req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doRequest(postHandlers, req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doRequest(putHandlers, req, resp);
    }
}
