package ru.bookshop.repository.impl;

import org.springframework.stereotype.Repository;
import ru.bookshop.entity.Book;
import ru.bookshop.repository.BookRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class BookRepositoryImpl implements BookRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Book add(Book book) {
        entityManager.persist(book);
        return book;
    }

    @Override
    public List<Book> get(String name, String authorName) {
        StringBuilder sb = new StringBuilder("select b from Book b where 1=1");

        if (name != null) {
            sb.append(" and lower(b.name) like lower('%' || :name || '%')");
        }

        if (authorName != null) {
            sb.append(" and lower(b.author.name) like lower('%' || :authorName || '%')");
        }

        TypedQuery<Book> query = entityManager.createQuery(sb.toString(), Book.class);

        if (name != null) {
            query.setParameter("name", name);
        }
        if (authorName != null) {
            query.setParameter("authorName", authorName);
        }

        return query.getResultList();
    }

    @Override
    public Book getById(Long id) {
        return entityManager.find(Book.class, id);
    }

    @Override
    public Book update(Book book) {
        entityManager.merge(book);
        return book;
    }

    @Override
    public void remove(Book book) {
        entityManager.remove(book);
    }
}
