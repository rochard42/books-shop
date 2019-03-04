package service.impl;

import entity.Author;
import entity.Book;
import exception.ApplicationException;
import exception.ErrorCode;
import repository.AuthorRepository;
import repository.impl.AuthorRepositoryImpl;
import service.AuthorService;

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
        Author author = new Author();
        author.setName(name);
        author.setDescription(description);

        return authorRepository.add(author);
    }

    @Override
    public Author getById(Long id) throws ApplicationException {
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
    public List<Book> getBooks(Long authorId) throws ApplicationException {
        Author author = getById(authorId);

        return author.getBooks() == null ? Collections.emptyList() : author.getBooks();
    }

    @Override
    public Author update(Long id, String name, String description) throws ApplicationException {
        Author author = getById(id);

        author.setName(name);
        author.setDescription(description);

        return authorRepository.update(author);
    }
}
