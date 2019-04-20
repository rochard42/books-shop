package ru.bookshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bookshop.ParameterNames;
import ru.bookshop.entity.Author;
import ru.bookshop.entity.Book;
import ru.bookshop.exception.ApplicationException;
import ru.bookshop.exception.ErrorCode;
import ru.bookshop.repository.AuthorRepository;
import ru.bookshop.repository.BookRepository;
import ru.bookshop.service.BookService;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    @Transactional
    public Book add(String name, String description, Long authorId) throws ApplicationException {
        Validator.checkNotNull(ParameterNames.NAME, name);
        Validator.checkNotNull(ParameterNames.DESCRIPTION, description);
        Validator.checkNotNull(ParameterNames.AUTHOR_ID, authorId);

        Author author = getAuthorById(authorId);

        Book book = new Book();
        book.setName(name);
        book.setDescription(description);
        book.setAuthor(author);

        return bookRepository.add(book);
    }

    @Override
    @Transactional
    public Book getById(long id) throws ApplicationException {
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
    @Transactional
    public List<Book> get(String name, String authorName) throws ApplicationException {
        return bookRepository.get(name, authorName);
    }

    @Override
    @Transactional
    public Book update(long id, String name, String description, Long authorId) throws ApplicationException {
        Validator.checkNotNull(ParameterNames.NAME, name);
        Validator.checkNotNull(ParameterNames.DESCRIPTION, description);
        Validator.checkNotNull(ParameterNames.AUTHOR_ID, authorId);

        Author author = getAuthorById(authorId);

        Book book = getById(id);

        book.setName(name);
        book.setDescription(description);
        book.setAuthor(author);

        return bookRepository.update(book);
    }

    @Override
    @Transactional
    public void remove(long id) throws  ApplicationException{
        Book book = getById(id);

        bookRepository.remove(book);
    }

    private Author getAuthorById(Long id) throws ApplicationException {
        Author author = authorRepository.getById(id);

        if (author == null) {
            throw new ApplicationException(
                    ErrorCode.INVALID_ARGUMENT,
                    String.format("Author with id %s not found", id)
            );
        }

        return author;
    }
}
