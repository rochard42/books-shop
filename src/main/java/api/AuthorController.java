package api;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Author;
import entity.Book;
import exception.ApplicationException;
import exception.ErrorCode;
import service.AuthorService;
import service.impl.AuthorServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@WebServlet(urlPatterns = Paths.AUTHORS + "/*")
public class AuthorController extends BaseController {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final Map<Pattern, RequestHandler> getHandlers = new HashMap<>();
    private final Map<Pattern, RequestHandler> postHandlers = new HashMap<>();
    private final Map<Pattern, RequestHandler> putHandlers = new HashMap<>();

    private final AuthorService authorService = AuthorServiceImpl.getInstance();

    public AuthorController() {
        getHandlers.put(Pattern.compile("^(/authors)/?$"), this::get);
        getHandlers.put(Pattern.compile("^(/authors/)([0-9]+)$"), this::getById);
        getHandlers.put(Pattern.compile("^(/authors/)([0-9]+)(/books)$"), this::getBooks);

        postHandlers.put(Pattern.compile("^(/authors)/?$"), this::add);

        putHandlers.put(Pattern.compile("^(/authors/)([0-9]+)$"), this::update);
    }

    private void get(List<String> urlParts, HttpServletRequest req, HttpServletResponse resp) throws ApplicationException, IOException {
        String name = req.getParameter("name");

        List<Author> authors = authorService.get(name);

        String response = OBJECT_MAPPER.writeValueAsString(authors);

        resp.getWriter().write(response);
    }

    private void getById(List<String> urlParts, HttpServletRequest req, HttpServletResponse resp) throws ApplicationException, IOException {
        Long id;
        try {
            id = Long.parseLong(urlParts.get(1));
        } catch (Exception e) {
            throw new ApplicationException(
                    ErrorCode.OBJECT_NOT_FOUND,
                    String.format("Author with id %s not found", urlParts.get(1)),
                    e
            );
        }

        Author author = authorService.getById(id);

        String response = OBJECT_MAPPER.writeValueAsString(author);

        resp.getWriter().write(response);
    }

    private void getBooks(List<String> urlParts, HttpServletRequest req, HttpServletResponse resp) throws ApplicationException, IOException {
        Long id;
        try {
            id = Long.parseLong(urlParts.get(1));
        } catch (Exception e) {
            throw new ApplicationException(
                    ErrorCode.OBJECT_NOT_FOUND,
                    String.format("Author with id %s not found", urlParts.get(1)),
                    e
            );
        }

        List<Book> books = authorService.getBooks(id);

        String response = OBJECT_MAPPER.writeValueAsString(books);

        resp.getWriter().write(response);
    }

    private void add(List<String> urlParts, HttpServletRequest req, HttpServletResponse resp) throws ApplicationException, IOException {
        Map<String, String> params = parseBody(req);
        String name = params.get("name");
        String description = params.get("description");

        Author author = authorService.add(name, description);

        String response = OBJECT_MAPPER.writeValueAsString(author);

        resp.getWriter().write(response);
    }

    private void update(List<String> urlParts, HttpServletRequest req, HttpServletResponse resp) throws ApplicationException, IOException {
        Long id;
        try {
            id = Long.parseLong(urlParts.get(1));
        } catch (Exception e) {
            throw new ApplicationException(
                    ErrorCode.OBJECT_NOT_FOUND,
                    String.format("Author with id %s not found", urlParts.get(1)),
                    e
            );
        }

        Map<String, String> params = parseBody(req);
        String name = params.get("name");
        String description = params.get("description");

        Author author = authorService.update(id, name, description);

        String response = OBJECT_MAPPER.writeValueAsString(author);

        resp.getWriter().write(response);
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
