package ru.bookshop.service;

import ru.bookshop.entity.Author;
import ru.bookshop.entity.Book;
import ru.bookshop.exception.ApplicationException;

import java.util.List;

public interface AuthorService {

    Author add(String name, String description) throws ApplicationException;

    Author getById(long id) throws ApplicationException;

    List<Author> get(String name) throws ApplicationException;

    List<Book> getBooks(long authorId) throws ApplicationException;

    Author update(long id, String name, String description) throws ApplicationException;
}
