package ru.bookshop.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bookshop.ParameterNames;
import ru.bookshop.entity.Author;
import ru.bookshop.entity.Book;
import ru.bookshop.exception.ApplicationException;
import ru.bookshop.service.AuthorService;

import javax.ws.rs.*;
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
    public List<Author> get(@QueryParam(ParameterNames.NAME) String name) throws ApplicationException {
        return authorService.get(name);
    }

    @GET
    @Path(Paths.ID)
    public Author getById(@PathParam(ParameterNames.ID) Long id) throws ApplicationException {
        return authorService.getById(id);
    }

    @GET
    @Path(Paths.ID + Paths.BOOKS)
    public List<Book> getBooks(@PathParam(ParameterNames.ID) Long id) throws ApplicationException {
        return authorService.getBooks(id);
    }

    @POST
    public Author add(
            @FormParam(ParameterNames.NAME) String name,
            @FormParam(ParameterNames.DESCRIPTION) String description
    ) throws ApplicationException {
        return authorService.add(name, description);
    }

    @PUT
    @Path(Paths.ID)
    public Author update(
            @PathParam(ParameterNames.ID) Long id,
            @FormParam(ParameterNames.NAME) String name,
            @FormParam(ParameterNames.DESCRIPTION) String description
    ) throws ApplicationException {
        return authorService.update(id, name, description);
    }
}
