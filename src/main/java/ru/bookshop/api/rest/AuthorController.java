package ru.bookshop.api.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bookshop.api.Paths;
import ru.bookshop.entity.Author;
import ru.bookshop.exception.ApplicationException;
import ru.bookshop.service.AuthorService;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path(Paths.AUTHORS)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GET
    public List<Author> get() throws ApplicationException {
        return authorService.get(null);
    }

}
