package repository;

import entity.Book;
import exception.ApplicationException;

import java.util.List;

public interface BookRepository {

    Book add(Book book) throws ApplicationException;

    List<Book> get(String name, String authorName) throws ApplicationException;

    Book getById(Long id) throws ApplicationException;

    Book update(Book book) throws ApplicationException;

    void remove(Book book) throws ApplicationException;
}
