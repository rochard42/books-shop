package ru.bookshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bookshop.ParameterNames;
import ru.bookshop.entity.Author;
import ru.bookshop.entity.Book;
import ru.bookshop.exception.ApplicationException;
import ru.bookshop.exception.ErrorCode;
import ru.bookshop.repository.AuthorRepository;
import ru.bookshop.service.AuthorService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    @Transactional
    public Author add(String name, String description) throws ApplicationException {
        Validator.checkNotNull(ParameterNames.NAME, name);
        Validator.checkNotNull(ParameterNames.DESCRIPTION, description);

        Author author = new Author();
        author.setName(name);
        author.setDescription(description);

        try {
            return authorRepository.add(author);
        } catch (Exception e) {
            throw ExceptionTranslator.translateDataAccessException(e);
        }
    }

    @Override
    @Transactional
    public Author getById(long id) throws ApplicationException {
        Author author;
        try {
            author = authorRepository.getById(id);
        } catch (Exception e) {
            throw ExceptionTranslator.translateDataAccessException(e);
        }

        if (author == null) {
            throw new ApplicationException(
                    ErrorCode.OBJECT_NOT_FOUND,
                    String.format("Author with id %s not found", id)
            );
        }

        return author;
    }

    @Override
    @Transactional
    public List<Author> get(String name) throws ApplicationException {
        try {
            return authorRepository.get(name);
        } catch (Exception e) {
            throw ExceptionTranslator.translateDataAccessException(e);
        }
    }

    @Override
    @Transactional
    public List<Book> getBooks(long authorId) throws ApplicationException {
        Author author = getById(authorId);

        return author.getBooks() == null ? Collections.emptyList() : new ArrayList<>(author.getBooks());
    }

    @Override
    @Transactional
    public Author update(long id, String name, String description) throws ApplicationException {
        Validator.checkNotNull(ParameterNames.NAME, name);
        Validator.checkNotNull(ParameterNames.DESCRIPTION, description);

        Author author = getById(id);

        author.setName(name);
        author.setDescription(description);

        try {
            return authorRepository.update(author);
        } catch (Exception e) {
            throw ExceptionTranslator.translateDataAccessException(e);
        }
    }
}
