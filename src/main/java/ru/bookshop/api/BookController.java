package ru.bookshop.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bookshop.ParameterNames;
import ru.bookshop.entity.Book;
import ru.bookshop.exception.ApplicationException;
import ru.bookshop.service.BookService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path(Paths.BOOKS)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GET
    public List<Book> get(
            @QueryParam(ParameterNames.NAME) String name,
            @QueryParam(ParameterNames.AUTHOR_NAME) String authorName
    ) throws ApplicationException {
        return bookService.get(name, authorName);
    }

    @GET
    @Path(Paths.ID)
    public Book getById(@PathParam(ParameterNames.ID) Long id) throws ApplicationException {
        return bookService.getById(id);
    }

    @POST
    public Book add(
            @FormParam(ParameterNames.NAME) String name,
            @FormParam(ParameterNames.DESCRIPTION) String description,
            @FormParam(ParameterNames.AUTHOR_ID) Long authorId
    ) throws ApplicationException {
        return bookService.add(name, description, authorId);
    }

    @PUT
    @Path(Paths.ID)
    public Book update(
            @PathParam(ParameterNames.ID) Long id,
            @FormParam(ParameterNames.NAME) String name,
            @FormParam(ParameterNames.DESCRIPTION) String description,
            @FormParam(ParameterNames.AUTHOR_ID) Long authorId) throws ApplicationException {
        return bookService.update(id, name, description, authorId);
    }

    @DELETE
    @Path(Paths.ID)
    public void delete(@PathParam(ParameterNames.ID) Long id) throws ApplicationException{
        bookService.remove(id);
    }
}


