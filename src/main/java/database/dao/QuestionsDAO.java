package database.dao;

import database.dataSets.QuestionsDataSet;
import database.executor.TExecutor;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by dima on 12.11.15.
 */
public class QuestionsDAO {
    private Connection connection;

    public QuestionsDAO(Connection connection) {
        this.connection = connection;
    }

    public QuestionsDataSet get(int id) throws SQLException {
        TExecutor exec = new TExecutor();
        return exec.execQuery(connection, "select * from question where id=" + id, result -> {
            result.next();
            return new QuestionsDataSet(result.getInt("id"), result.getString("text"));
        });
    }
}
