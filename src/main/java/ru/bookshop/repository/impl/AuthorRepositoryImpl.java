package ru.bookshop.repository.impl;

import org.springframework.stereotype.Repository;
import ru.bookshop.entity.Author;
import ru.bookshop.repository.AuthorRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class AuthorRepositoryImpl implements AuthorRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Author add(Author author) {
        entityManager.persist(author);
        return author;
    }

    @Override
    public List<Author> get(String name) {
        StringBuilder sb = new StringBuilder("select a from Author a");
        if (name != null) {
            sb.append(" where lower(a.name) like lower('%' || :name || '%')");
        }

        TypedQuery<Author> query = entityManager.createQuery(sb.toString(), Author.class);

        if (name != null) {
            query.setParameter("name", name);
        }

        return query.getResultList();
    }

    @Override
    public Author getById(Long id) {
        return entityManager.find(Author.class, id);
    }

    @Override
    public Author update(Author author) {
        entityManager.merge(author);
        return author;
    }
}
