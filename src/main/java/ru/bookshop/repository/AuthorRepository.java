package ru.bookshop.repository;

import ru.bookshop.entity.Author;
import ru.bookshop.exception.ApplicationException;

import java.util.List;

public interface AuthorRepository {

    Author add(Author author) throws ApplicationException;

    List<Author> get(String name) throws ApplicationException;

    Author getById(Long id) throws ApplicationException;

    Author update(Author author) throws ApplicationException;
}
