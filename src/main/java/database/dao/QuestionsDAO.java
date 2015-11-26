package database.dao;

import database.data.QuestionsDataSet;
import database.executor.TExecutor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

/**
 * Created by dima on 12.11.15.
 */
public class QuestionsDAO {
    private final Connection connection;

    private static final String TABLE_NAME = "questions";

    private static final String COL_ID = "id";
    private static final String COL_TEXT = "title";
    private static final String COL_TYPE = "type";

    public QuestionsDAO(Connection connection) {
        this.connection = connection;
    }

    @SuppressWarnings("unused")
    public QuestionsDataSet get(int id) throws SQLException {
        String query = "SELECT *" +
                " FROM " + TABLE_NAME +
                " WHERE " + COL_ID + " = " + id + ';';

        return TExecutor.execQuery(connection, query, result -> {
            result.next();
            return new QuestionsDataSet(result.getInt(COL_ID),
                    result.getString(COL_TEXT),
                    result.getInt(COL_TYPE));
        });
    }

    public QuestionsDataSet getRandom(int type, Set<Integer> askedQuestions) throws SQLException {
        StringBuilder alreadyAskedBuilder = new StringBuilder();
        alreadyAskedBuilder.append('(');
        if (askedQuestions.isEmpty()) {
            alreadyAskedBuilder.append("-1,");
        }
        for (int id : askedQuestions) {
            alreadyAskedBuilder.append(id).append(',');
        }
        alreadyAskedBuilder.deleteCharAt(alreadyAskedBuilder.length() - 1);
        alreadyAskedBuilder.append(')');

        String randOffsetQuery = "SELECT FLOOR(RAND() * COUNT(*)) as \'offset\'" +
                " FROM " + TABLE_NAME +
                " WHERE type = " + type +
                " AND id NOT IN " + alreadyAskedBuilder;

        int randOffset = TExecutor.execQuery(connection, randOffsetQuery, result -> {
            result.next();
            return result.getInt("offset");
        });

        String query = "SELECT *" +
                " FROM " + TABLE_NAME +
                " WHERE type = " + type +
                " AND id NOT IN " + alreadyAskedBuilder +
                " LIMIT " + randOffset + ", 1;";

        return TExecutor.execQuery(connection, query, result -> {
            result.next();
            return new QuestionsDataSet(result.getInt(COL_ID),
                    result.getString(COL_TEXT),
                    result.getInt(COL_TYPE));
        });
    }
}
