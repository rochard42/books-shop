package service;

import entity.Author;
import entity.Book;
import exception.ApplicationException;

import java.util.List;

public interface AuthorService {

    Author add(String name, String description) throws ApplicationException;

    Author getById(Long id) throws ApplicationException;

    List<Author> get(String name) throws ApplicationException;

    List<Book> getBooks(Long authorId) throws ApplicationException;

    Author update(Long id, String name, String description) throws ApplicationException;
}
