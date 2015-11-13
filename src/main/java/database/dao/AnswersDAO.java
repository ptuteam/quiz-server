package database.dao;

import database.data.AnswersDataSet;
import database.executor.TExecutor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by dima on 12.11.15.
 */
public class AnswersDAO {
    private final Connection connection;

    private static final String TABLE_NAME = "answer";

    // column names
    private static final String COL_ID = "id";
    private static final String COL_TEXT = "text";
    private static final String COL_QUESTION_ID = "question_id";
    private static final String COL_IS_CORRECT = "is_correct";

    public AnswersDAO(Connection connection) {
        this.connection = connection;
    }

    public AnswersDataSet get(int id) throws SQLException {
        String query = "SELECT *" +
                " FROM " + TABLE_NAME +
                " WHERE " + COL_ID + " = " + id + ';';

        TExecutor exec = new TExecutor();
        return exec.execQuery(connection, query, result -> {
            result.next();
            return new AnswersDataSet(
                    result.getInt(COL_ID),
                    result.getString(COL_TEXT),
                    result.getInt(COL_QUESTION_ID),
                    result.getBoolean(COL_IS_CORRECT));
        });
    }

    public ArrayList<AnswersDataSet> getByQuestionId(int questionId) throws SQLException {
        String query = "SELECT *" +
                " FROM " + TABLE_NAME +
                " WHERE " + COL_QUESTION_ID + " = " + questionId + ';';

        ArrayList<AnswersDataSet> resultArray = new ArrayList<>();
        TExecutor exec = new TExecutor();
        return exec.execQuery(connection, query, result -> {
            while(result.next())
            {
                resultArray.add(new AnswersDataSet(
                        result.getInt(COL_ID),
                        result.getString(COL_TEXT),
                        result.getInt(COL_QUESTION_ID),
                        result.getBoolean(COL_IS_CORRECT)));
            }
            return resultArray;
        });
    }

    public AnswersDataSet getCorrectByQuestionId(int questionId) throws SQLException {
        String query =  "SELECT *" +
                        " FROM " + TABLE_NAME +
                        " WHERE " + COL_QUESTION_ID + " = " + questionId +
                        " AND " + COL_IS_CORRECT + " = true;";

        TExecutor exec = new TExecutor();
        return exec.execQuery(connection, query, result -> {
            result.next();
            return new AnswersDataSet(
                    result.getInt(COL_ID),
                    result.getString(COL_TEXT),
                    result.getInt(COL_QUESTION_ID),
                    result.getBoolean(COL_IS_CORRECT));
        });
    }
}
