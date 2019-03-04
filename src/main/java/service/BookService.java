package service;

import entity.Book;
import exception.ApplicationException;

import java.util.List;

public interface BookService {

    Book add(String name, String description, Long author) throws ApplicationException;

    Book update(Long id, String name, String description, Long authorId) throws ApplicationException;

    Book getById(Long id) throws ApplicationException;

    List<Book> get(String name, String authorName) throws ApplicationException;

    void remove(Long id) throws ApplicationException;

}
