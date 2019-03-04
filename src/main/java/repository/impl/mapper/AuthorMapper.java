package repository.impl.mapper;

import entity.Author;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class AuthorMapper {

    private final static String ID = "id";
    private final static String NAME = "name";
    private final static String DESCRIPTION = "description";

    public static Author map(String prefix, ResultSet rs) throws SQLException {
        Author author = new Author();
        author.setId(rs.getLong(prefix + ID));
        author.setName(rs.getString(prefix + NAME));
        author.setDescription(rs.getString(prefix + DESCRIPTION));

        return author;
    }

    public static Author map(ResultSet rs) throws SQLException {
        return map("", rs);
    }

    private AuthorMapper() {
    }
}
