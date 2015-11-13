package database.dao;

import database.data.QuestionsDataSet;
import database.executor.TExecutor;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by dima on 12.11.15.
 */
public class QuestionsDAO {
    private final Connection connection;

    private static final String TABLE_NAME = "question";

    // column names
    private static final String COL_ID = "id";
    private static final String COL_TEXT = "text";
    private static final String COL_TYPE = "type";

    public QuestionsDAO(Connection connection) {
        this.connection = connection;
    }

    public QuestionsDataSet get(int id) throws SQLException {
        String query = "SELECT *" +
                " FROM " + TABLE_NAME +
                " WHERE " + COL_ID + " = " + id + ';';

        TExecutor exec = new TExecutor();
        return exec.execQuery(connection, query, result -> {
            result.next();
            return new QuestionsDataSet(result.getInt(COL_ID),
                    result.getString(COL_TEXT),
                    result.getInt(COL_TYPE));
        });
    }
}
