package database.connection;

import org.apache.commons.dbcp.BasicDataSource;
import utils.ConfigGeneral;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by dima on 12.11.15.
 */
public class DatabaseSource {

    private static DataSource quizDataSource;
    private static DataSource usersDataSource;

    public static DataSource getQuizDataSource() {
        if (quizDataSource != null) {
            return quizDataSource;
        }

        quizDataSource = getDataSource(ConfigGeneral.getDbQuizUrl());
        return quizDataSource;
    }

    public static DataSource getUsersDataSource() {
        if (usersDataSource != null) {
            return usersDataSource;
        }

        usersDataSource = getDataSource(ConfigGeneral.getDbUsersUrl());
        return usersDataSource;
    }

    private static DataSource getDataSource(String dbUrl) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(ConfigGeneral.getDbDriver());
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(ConfigGeneral.getDbUsername());
        dataSource.setPassword(ConfigGeneral.getDbPassword());
        dataSource.setValidationQuery(ConfigGeneral.getDbValidationQuery());
        dataSource.setMaxActive(ConfigGeneral.getDbMaxActive());
        dataSource.setInitialSize(ConfigGeneral.getDbInitialSize());

        return dataSource;
    }

    public static boolean checkQuizConnection() {
        try (Connection connection = getQuizDataSource().getConnection()) {

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean checkUsersConnection() {
        try (Connection connection = getUsersDataSource().getConnection()) {

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
