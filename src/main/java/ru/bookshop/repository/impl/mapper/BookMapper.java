package ru.bookshop.repository.impl.mapper;

import ru.bookshop.entity.Book;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookMapper {

    private final static String ID = "id";
    private final static String NAME = "name";
    private final static String DESCRIPTION = "description";

    public static Book map(String prefix, ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setId(rs.getLong(prefix + ID));
        book.setName(rs.getString(prefix + NAME));
        book.setDescription(rs.getString(prefix + DESCRIPTION));

        return book;
    }

    public static Book map(ResultSet rs) throws SQLException {
        return map("", rs);
    }

    private BookMapper() {
    }
}
