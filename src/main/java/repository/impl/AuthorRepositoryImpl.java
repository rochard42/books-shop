package repository.impl;

import entity.Author;
import entity.Book;
import exception.ApplicationException;
import exception.ErrorCode;
import repository.AuthorRepository;
import repository.impl.mapper.AuthorMapper;
import repository.impl.mapper.BookMapper;

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
    @SuppressWarnings("Duplicates")
    public Author add(Author author) throws ApplicationException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = JdbcUtils.getConnection();

            ps = con.prepareStatement(
                    "insert into author (name, description) values (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, author.getName());
            ps.setString(2, author.getDescription());

            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (!rs.next()) {
                throw new ApplicationException(
                        ErrorCode.INTERNAL_ERROR,
                        "Internal error"
                );
            }

            author.setId(rs.getLong("id"));

            return author;
        } catch (SQLException e) {
            throw JdbcUtils.translateSQLException(e);
        } finally {
            JdbcUtils.closeQuietly(rs, ps, con);
        }
    }

    @Override
    public List<Author> get(String name) throws ApplicationException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = JdbcUtils.getConnection();

            StringBuilder sb = new StringBuilder("select id, name, description from author");
            if (name != null) {
                sb.append(" where lower(name) like lower('%' || ? || '%')");
            }

            ps = con.prepareStatement(sb.toString());
            if (name != null) {
                ps.setString(1, name);
            }

            rs = ps.executeQuery();

            List<Author> result = new ArrayList<>();

            while (rs.next()) {
                result.add(AuthorMapper.map(rs));
            }

            return result;
        } catch (SQLException e) {
            throw JdbcUtils.translateSQLException(e);
        } finally {
            JdbcUtils.closeQuietly(rs, ps, con);
        }
    }

    @Override
    public Author getById(Long id) throws ApplicationException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = JdbcUtils.getConnection();

            ps = con.prepareStatement(
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

            rs = ps.executeQuery();

            Author author = null;
            while (rs.next()) {
                if (author == null) {
                    author = AuthorMapper.map("author_", rs);
                    author.setBooks(new ArrayList<>());
                }

                if (rs.getObject("book_id") != null) {
                    Book book = BookMapper.map("book_", rs);
                    book.setAuthor(author);

                    author.getBooks().add(book);
                }
            }

            return author;
        } catch (SQLException e) {
            throw JdbcUtils.translateSQLException(e);
        } finally {
            JdbcUtils.closeQuietly(rs, ps, con);
        }
    }

    @Override
    public Author update(Author author) throws ApplicationException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = JdbcUtils.getConnection();

            ps = con.prepareStatement("update author set name = ?, description = ? where id = ?");
            ps.setString(1, author.getName());
            ps.setString(2, author.getDescription());
            ps.setLong(3, author.getId());

            ps.executeUpdate();

            return author;
        } catch (SQLException e) {
            throw JdbcUtils.translateSQLException(e);
        } finally {
            JdbcUtils.closeQuietly(ps, con);
        }
    }
}
