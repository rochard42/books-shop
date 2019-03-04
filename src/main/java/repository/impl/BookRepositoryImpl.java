package repository.impl;

import entity.Author;
import entity.Book;
import exception.ApplicationException;
import exception.ErrorCode;
import repository.BookRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookRepositoryImpl implements BookRepository {

    private static final BookRepository INSTANCE = new BookRepositoryImpl();

    private BookRepositoryImpl() {
    }

    public static BookRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public Book add(Book book) throws ApplicationException {
        try {
            Connection connection = JdbcUtils.getConnection();

            PreparedStatement ps = connection.prepareStatement(
                    "insert into book (name, description, author) values (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, book.getName());
            ps.setString(2, book.getDescription());
            ps.setLong(3, book.getAuthor().getId());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (!rs.next()) {
                throw new ApplicationException(
                        ErrorCode.INTERNAL_ERROR,
                        "internal error"
                );
            }

            book.setId(rs.getLong("id"));

            JdbcUtils.closeResultSet(rs);
            return book;
        } catch (SQLException e) {
            throw JdbcUtils.translateSQLException(e);
        }
    }

    @Override
    public Book update(Book book) throws ApplicationException {
        try {
            Connection connection = JdbcUtils.getConnection();

            PreparedStatement ps = connection.prepareStatement(
                    "update book set name = ?, description = ?, author = ? where id = ?"
            );
            ps.setString(1, book.getName());
            ps.setString(2, book.getDescription());
            ps.setLong(3, book.getAuthor().getId());
            ps.setLong(4, book.getId());

            ps.executeUpdate();

            JdbcUtils.closeStatement(ps);
            return book;
        } catch (SQLException e) {
            throw JdbcUtils.translateSQLException(e);
        }
    }

    @Override
    public Book getById(Long id) throws ApplicationException {

        try {
            Connection connection = JdbcUtils.getConnection();

            PreparedStatement ps = connection.prepareStatement(
                    "select a.id author_id," +
                            "   a.name author_name," +
                            "   a.description author_description," +
                            "   b.id book_id," +
                            "   b.name book_name," +
                            "   b.description book_description" +
                            "   from book b left join author a on b.author = a.id" +
                            "   where b.id = ?"
            );

            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();

            Book book = null;
            if (rs.next()) {
                Author author = new Author();
                author.setId(rs.getLong("author_id"));
                author.setName(rs.getString("author_name"));
                author.setDescription(rs.getString("author_description"));

                book = new Book();
                book.setId(rs.getLong("book_id"));
                book.setName(rs.getString("book_name"));
                book.setDescription(rs.getString("book_description"));
                book.setAuthor(author);
            }

            JdbcUtils.closeResultSet(rs);
            return book;

        } catch (SQLException e) {
            throw JdbcUtils.translateSQLException(e);
        }
    }

    @Override
    public void remove(Long id) throws ApplicationException {

        try {
            Connection connection = JdbcUtils.getConnection();

            PreparedStatement ps = connection.prepareStatement(
                    "delete from book where id = ?"
            );

            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();

            JdbcUtils.closeResultSet(rs);

        } catch (SQLException e) {
            throw JdbcUtils.translateSQLException(e);
        }
    }

    @Override
    public List<Book> get(String name, String authorName) throws ApplicationException {


        try {
            Connection connection = JdbcUtils.getConnection();

            StringBuilder sb = new StringBuilder("select");

            sb.append(" b.id book_id, b.name book_name, b.description book_description,");
            sb.append(" a.id author_id, a.name author_name, a.description author_description");
            sb.append(" from book b left join author a on b.author = a.id where 1=1");

            if (name != null) {
                sb.append(" and lower(b.name) like lower('%' || ? || '%')");
            }
            if (authorName != null) {
                sb.append(" and lower(a.name) like lower('%' || ? || '%')");
            }

            PreparedStatement ps = connection.prepareStatement(sb.toString());

            int paramIndex = 1;
            if (name != null) {
                ps.setString(paramIndex++, name);
            }
            if (authorName != null) {
                ps.setString(paramIndex++, authorName);
            }

            ResultSet rs = ps.executeQuery();

            List<Book> books = new ArrayList<>();
            Author author = null;
            Book book = null;

            while (rs.next()) {
                author = new Author();
                author.setId(rs.getLong("author_id"));
                author.setName(rs.getString("author_name"));
                author.setDescription(rs.getString("author_description"));

                book = new Book();
                book.setName(rs.getString("book_name"));
                book.setId(rs.getLong("book_id"));
                book.setDescription(rs.getString("book_description"));
                book.setAuthor(author);

                books.add(book);
            }
            return books;
        } catch (SQLException e) {
            throw JdbcUtils.translateSQLException(e);
        }
    }
}
