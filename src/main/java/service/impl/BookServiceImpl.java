package service.impl;

import entity.Author;
import entity.Book;
import exception.ApplicationException;
import exception.ErrorCode;
import repository.AuthorRepository;
import repository.BookRepository;
import repository.impl.AuthorRepositoryImpl;
import repository.impl.BookRepositoryImpl;
import service.BookService;

import java.util.ArrayList;
import java.util.List;

public class BookServiceImpl implements BookService {

    private static final BookService INSTANCE = new BookServiceImpl();

    private final BookRepository bookRepository = BookRepositoryImpl.getInstance();

    private final AuthorRepository authorRepository = AuthorRepositoryImpl.getInstance();

    private BookServiceImpl() {
    }

    public static BookService getInstance() {
        return INSTANCE;
    }

    @Override
    public Book add(String name, String description, Long authorId) throws ApplicationException {
        Author author = authorRepository.getById(authorId);

        if (author == null) {
            throw new ApplicationException(ErrorCode.INVALID_ARGUMENT, "Author with id %s not found");
        }

        Book book = new Book();

        book.setName(name);
        book.setDescription(description);
        book.setAuthor(author);

        return bookRepository.add(book);
    }

    @Override
    public Book update(Long id, String name, String description, Long authorId) throws ApplicationException {
        Author author = authorRepository.getById(authorId);

        if (author == null) {
            throw new ApplicationException(ErrorCode.INVALID_ARGUMENT, "Author with id %s not found");
        }

        Book book = getById(id);

        book.setName(name);
        book.setDescription(description);
        book.setAuthor(author);

        return bookRepository.update(book);
    }

    @Override
    public Book getById(Long id) throws ApplicationException {
        Book book = bookRepository.getById(id);

        if (book == null) {
            throw new ApplicationException(
                    ErrorCode.OBJECT_NOT_FOUND,
                    String.format("Book with id %s not found", id)
            );
        }

        return book;
    }

    @Override
    public List<Book> get(String name, String authorName) throws ApplicationException {
        return bookRepository.get(name, authorName);
    }

    @Override
    public void remove(Long id) throws  ApplicationException{
        bookRepository.remove(id);
    }
}
