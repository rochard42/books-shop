package ru.bookshop.service.impl;

import ru.bookshop.ParameterNames;
import ru.bookshop.entity.Author;
import ru.bookshop.entity.Book;
import ru.bookshop.exception.ApplicationException;
import ru.bookshop.exception.ErrorCode;
import ru.bookshop.repository.AuthorRepository;
import ru.bookshop.repository.impl.AuthorRepositoryImpl;
import ru.bookshop.service.AuthorService;

import java.util.Collections;
import java.util.List;

public class AuthorServiceImpl implements AuthorService {

    private static final AuthorService INSTANCE = new AuthorServiceImpl();

    private final AuthorRepository authorRepository = AuthorRepositoryImpl.getInstance();

    private AuthorServiceImpl() {
    }

    public static AuthorService getInstance() {
        return INSTANCE;
    }

    @Override
    public Author add(String name, String description) throws ApplicationException {
        Validator.checkNotNull(ParameterNames.NAME, name);
        Validator.checkNotNull(ParameterNames.DESCRIPTION, description);

        Author author = new Author();
        author.setName(name);
        author.setDescription(description);

        return authorRepository.add(author);
    }

    @Override
    public Author getById(long id) throws ApplicationException {
        Author author = authorRepository.getById(id);

        if (author == null) {
            throw new ApplicationException(
                    ErrorCode.OBJECT_NOT_FOUND,
                    String.format("Author with id %s not found", id)
            );
        }

        return author;
    }

    @Override
    public List<Author> get(String name) throws ApplicationException {
        return authorRepository.get(name);
    }

    @Override
    public List<Book> getBooks(long authorId) throws ApplicationException {
        Author author = getById(authorId);

        return author.getBooks() == null ? Collections.emptyList() : author.getBooks();
    }

    @Override
    public Author update(long id, String name, String description) throws ApplicationException {
        Validator.checkNotNull(ParameterNames.NAME, name);
        Validator.checkNotNull(ParameterNames.DESCRIPTION, description);

        Author author = getById(id);

        author.setName(name);
        author.setDescription(description);

        return authorRepository.update(author);
    }
}
