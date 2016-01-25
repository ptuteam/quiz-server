package database.dao;

import database.data.AnswersDataSet;
import database.executor.TExecutor;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by dima on 12.11.15.
 */
public class AnswersDAO {
    private final DataSource dataSource;

    private static final String TABLE_NAME = "answers";

    private static final String COL_ID = "id";
    private static final String COL_TEXT = "answer";
    private static final String COL_QUESTION_ID = "q_id";
    private static final String COL_IS_CORRECT = "is_correct";

    public AnswersDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @SuppressWarnings("unused")
    public AnswersDataSet get(int id) throws SQLException {
        String query = "SELECT *" +
                " FROM " + TABLE_NAME +
                " WHERE " + COL_ID + " = " + id + ';';

        return TExecutor.execQuery(dataSource, query, result -> {
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

        return TExecutor.execQuery(dataSource, query, result -> {
            while (result.next()) {
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

        return TExecutor.execQuery(dataSource, query, result -> {
            result.next();
            return new AnswersDataSet(
                    result.getInt(COL_ID),
                    result.getString(COL_TEXT),
                    result.getInt(COL_QUESTION_ID),
                    result.getBoolean(COL_IS_CORRECT));
        });
    }
}
