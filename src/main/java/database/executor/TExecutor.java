package database.executor;

import database.handlers.TResultHandler;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by dima on 12.11.15.
 */
public class TExecutor {
    public static <T> T execQuery(DataSource dataSource, String query, TResultHandler<T> handler) throws SQLException {
        T value;
        try (Connection connection = dataSource.getConnection(); Statement stmt = connection.createStatement()) {
            stmt.execute(query);
            try (ResultSet result = stmt.getResultSet()) {
                value = handler.handle(result);
            }
        }

        return value;
    }

    public static void execQuery(DataSource dataSource, String query) throws SQLException {
        try (Connection connection = dataSource.getConnection(); Statement stmt = connection.createStatement()) {
            stmt.execute(query);
        }
    }
}
