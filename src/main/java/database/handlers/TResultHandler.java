package database.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by dima on 12.11.15.
 */
public interface TResultHandler<T> {
    T handle(ResultSet resultSet) throws SQLException;
}
