package database.dao;

import database.dataSets.AnswersDataSet;
import database.dataSets.QuestionsDataSet;
import database.executor.TExecutor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by dima on 12.11.15.
 */
public class AnswersDAO {
    private Connection connection;

    public AnswersDAO(Connection connection) {
        this.connection = connection;
    }

    public AnswersDataSet get(int id) throws SQLException {
        TExecutor exec = new TExecutor();
        return exec.execQuery(connection, "select * from answer where id=" + id, result -> {
            result.next();
            return new AnswersDataSet(
                    result.getInt("id"),
                    result.getString("text"),
                    result.getInt("question_id"),
                    result.getBoolean("is_correct"));
        });
    }

    public ArrayList<AnswersDataSet> getByQuestionId(int questionId) throws SQLException {
        ArrayList<AnswersDataSet> resultArray = new ArrayList<>();
        TExecutor exec = new TExecutor();
        return exec.execQuery(connection, "select * from answer where question_id = " + questionId, result -> {
            while(result.next())
            {
                resultArray.add(new AnswersDataSet(
                        result.getInt("id"),
                        result.getString("text"),
                        result.getInt("question_id"),
                        result.getBoolean("is_correct")));
            }
            return resultArray;
        });
    }


}
