package repository.impl;

import entity.Author;
import entity.Book;
import exception.ApplicationException;
import exception.ErrorCode;
import repository.BookRepository;
import repository.impl.mapper.AuthorMapper;
import repository.impl.mapper.BookMapper;

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
    @SuppressWarnings("Duplicates")
    public Book add(Book book) throws ApplicationException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = JdbcUtils.getConnection();

            ps = con.prepareStatement(
                    "insert into book (name, description, author) values (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, book.getName());
            ps.setString(2, book.getDescription());
            ps.setLong(3, book.getAuthor().getId());

            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (!rs.next()) {
                throw new ApplicationException(
                        ErrorCode.INTERNAL_ERROR,
                        "Internal error"
                );
            }

            book.setId(rs.getLong("id"));

            return book;
        } catch (SQLException e) {
            throw JdbcUtils.translateSQLException(e);
        } finally {
            JdbcUtils.closeQuietly(rs, ps, con);
        }
    }

    @Override
    public List<Book> get(String name, String authorName) throws ApplicationException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = JdbcUtils.getConnection();

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

            ps = con.prepareStatement(sb.toString());

            int paramIndex = 1;
            if (name != null) {
                ps.setString(paramIndex++, name);
            }
            if (authorName != null) {
                ps.setString(paramIndex++, authorName);
            }

            rs = ps.executeQuery();

            List<Book> books = new ArrayList<>();
            while (rs.next()) {
                Author author = AuthorMapper.map("author_", rs);
                Book book = BookMapper.map("book_", rs);
                book.setAuthor(author);

                books.add(book);
            }

            return books;
        } catch (SQLException e) {
            throw JdbcUtils.translateSQLException(e);
        } finally {
            JdbcUtils.closeQuietly(rs, ps, con);
        }
    }

    @Override
    public Book getById(Long id) throws ApplicationException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = JdbcUtils.getConnection();

            ps = con.prepareStatement(
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

            rs = ps.executeQuery();

            Book book = null;
            if (rs.next()) {
                Author author = AuthorMapper.map("author_", rs);
                book = BookMapper.map("book_", rs);
                book.setAuthor(author);
            }

            return book;
        } catch (SQLException e) {
            throw JdbcUtils.translateSQLException(e);
        } finally {
            JdbcUtils.closeQuietly(rs, ps, con);
        }
    }

    @Override
    public Book update(Book book) throws ApplicationException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = JdbcUtils.getConnection();

            ps = con.prepareStatement("update book set name = ?, description = ?, author = ? where id = ?");
            ps.setString(1, book.getName());
            ps.setString(2, book.getDescription());
            ps.setLong(3, book.getAuthor().getId());
            ps.setLong(4, book.getId());

            ps.executeUpdate();

            return book;
        } catch (SQLException e) {
            throw JdbcUtils.translateSQLException(e);
        } finally {
            JdbcUtils.closeQuietly(ps, con);
        }
    }

    @Override
    public void remove(Book book) throws ApplicationException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = JdbcUtils.getConnection();

            ps = con.prepareStatement("delete from book where id = ?");
            ps.setLong(1, book.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw JdbcUtils.translateSQLException(e);
        } finally {
            JdbcUtils.closeQuietly(ps, con);
        }
    }
}
