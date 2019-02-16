package repository.impl;

import entity.Author;
import entity.Book;
import exception.ApplicationException;
import exception.ErrorCode;
import repository.AuthorRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorRepositoryImpl implements AuthorRepository {

    private static final AuthorRepository INSTANCE = new AuthorRepositoryImpl();

    private AuthorRepositoryImpl() {
    }

    public static AuthorRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public Author add(Author author) throws ApplicationException {
        try {
            Connection connection = JdbcUtils.getConnection();

            PreparedStatement ps = connection.prepareStatement(
                    "insert into author (name, description) values (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, author.getName());
            ps.setString(2, author.getDescription());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (!rs.next()) {
                throw new ApplicationException(
                        ErrorCode.INTERNAL_ERROR,
                        "Internal error"
                );
            }

            author.setId(rs.getLong("id"));

            JdbcUtils.closeResultSet(rs);
            return author;
        } catch (SQLException e) {
            throw JdbcUtils.translateSQLException(e);
        }
    }

    @Override
    public Author update(Author author) throws ApplicationException {
        try {
            Connection connection = JdbcUtils.getConnection();

            PreparedStatement ps = connection.prepareStatement(
                    "update author set name = ?, description = ? where id = ?"
            );
            ps.setString(1, author.getName());
            ps.setString(2, author.getDescription());
            ps.setLong(3, author.getId());

            ps.executeUpdate();

            JdbcUtils.closeStatement(ps);
            return author;
        } catch (SQLException e) {
            throw JdbcUtils.translateSQLException(e);
        }
    }

    @Override
    public List<Author> get(String name) throws ApplicationException {
        try {
            Connection connection = JdbcUtils.getConnection();

            StringBuilder sb = new StringBuilder("select id, name, description from author");
            if (name != null) {
                sb.append(" where lower(name) like lower('%' || ? || '%')");
            }

            PreparedStatement ps = connection.prepareStatement(sb.toString());
            if (name != null) {
                ps.setString(1, name);
            }

            ResultSet rs = ps.executeQuery();

            List<Author> result = new ArrayList<>();

            while (rs.next()) {
                Author author = new Author();
                author.setId(rs.getLong("id"));
                author.setName(rs.getString("name"));
                author.setDescription(rs.getString("description"));

                result.add(author);
            }

            JdbcUtils.closeResultSet(rs);
            return result;
        } catch (SQLException e) {
            throw JdbcUtils.translateSQLException(e);
        }
    }

    @Override
    public Author getById(Long id) throws ApplicationException {
        try {
            Connection connection = JdbcUtils.getConnection();

            PreparedStatement ps = connection.prepareStatement(
                    "select a.id author_id," +
                        "       a.name author_name," +
                        "       a.description author_description," +
                        "       b.id book_id," +
                        "       b.name book_name," +
                        "       b.description book_description" +
                        "  from author a left join book b on a.id = b.author" +
                        " where a.id = ?"
            );
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();

            Author author = null;
            while (rs.next()) {
                if (author == null) {
                    author = new Author();
                    author.setId(rs.getLong("author_id"));
                    author.setName(rs.getString("author_name"));
                    author.setDescription(rs.getString("author_description"));
                    author.setBooks(new ArrayList<>());
                }

                long bookId = rs.getLong("book_id");
                if (!rs.wasNull()) {
                    Book book = new Book();
                    book.setId(bookId);
                    book.setName(rs.getString("book_name"));
                    book.setDescription(rs.getString("book_description"));
                    book.setAuthor(author);

                    author.getBooks().add(book);
                }
            }

            JdbcUtils.closeResultSet(rs);
            return author;
        } catch (SQLException e) {
            throw JdbcUtils.translateSQLException(e);
        }
    }

}
