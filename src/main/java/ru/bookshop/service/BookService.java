package ru.bookshop.service;

import ru.bookshop.entity.Book;
import ru.bookshop.exception.ApplicationException;

import java.util.List;

public interface BookService {

    Book add(String name, String description, Long author) throws ApplicationException;

    Book getById(long id) throws ApplicationException;

    List<Book> get(String name, String authorName) throws ApplicationException;

    Book update(long id, String name, String description, Long authorId) throws ApplicationException;

    void remove(long id) throws ApplicationException;
}
