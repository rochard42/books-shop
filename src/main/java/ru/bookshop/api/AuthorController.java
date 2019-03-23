package ru.bookshop.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.bookshop.ParameterNames;
import ru.bookshop.entity.Author;
import ru.bookshop.entity.Book;
import ru.bookshop.exception.ApplicationException;
import ru.bookshop.service.AuthorService;

import javax.servlet.ServletConfig;
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

    @Autowired
    private AuthorService authorService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    public AuthorController() {
        getHandlers.put(Pattern.compile("^(/authors)/?$"), this::get);
        getHandlers.put(Pattern.compile("^(/authors/)([0-9]+)$"), this::getById);
        getHandlers.put(Pattern.compile("^(/authors/)([0-9]+)(/books)$"), this::getBooks);

        postHandlers.put(Pattern.compile("^(/authors)/?$"), this::add);

        putHandlers.put(Pattern.compile("^(/authors/)([0-9]+)$"), this::update);
    }

    private void get(List<String> urlParts, HttpServletRequest req, HttpServletResponse resp) throws ApplicationException, IOException {
        String name = req.getParameter(ParameterNames.NAME);

        List<Author> authors = authorService.get(name);

        String response = OBJECT_MAPPER.writeValueAsString(authors);

        resp.getWriter().write(response);
    }

    private void getById(List<String> urlParts, HttpServletRequest req, HttpServletResponse resp) throws ApplicationException, IOException {
        long id = ParameterParser.parseLongInUrl("Author", urlParts.get(1));

        Author author = authorService.getById(id);

        String response = OBJECT_MAPPER.writeValueAsString(author);

        resp.getWriter().write(response);
    }

    private void getBooks(List<String> urlParts, HttpServletRequest req, HttpServletResponse resp) throws ApplicationException, IOException {
        long id = ParameterParser.parseLongInUrl("Author", urlParts.get(1));

        List<Book> books = authorService.getBooks(id);

        String response = OBJECT_MAPPER.writeValueAsString(books);

        resp.getWriter().write(response);
    }

    private void add(List<String> urlParts, HttpServletRequest req, HttpServletResponse resp) throws ApplicationException, IOException {
        Map<String, String> params = parseBody(req);
        String name = params.get(ParameterNames.NAME);
        String description = params.get(ParameterNames.DESCRIPTION);

        Author author = authorService.add(name, description);

        String response = OBJECT_MAPPER.writeValueAsString(author);

        resp.getWriter().write(response);
    }

    private void update(List<String> urlParts, HttpServletRequest req, HttpServletResponse resp) throws ApplicationException, IOException {
        long id = ParameterParser.parseLongInUrl("Author", urlParts.get(1));

        Map<String, String> params = parseBody(req);
        String name = params.get(ParameterNames.NAME);
        String description = params.get(ParameterNames.DESCRIPTION);

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
