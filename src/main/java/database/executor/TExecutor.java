package database.executor;

import database.handlers.TResultHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by dima on 12.11.15.
 */
public class TExecutor {
    public <T> T execQuery(Connection connection, String query, TResultHandler<T> handler) throws SQLException {
        T value;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(query);
            try (ResultSet result = stmt.getResultSet()) {
                value = handler.handle(result);

                result.close();
            }
            stmt.close();
        }

        return value;
    }
}
