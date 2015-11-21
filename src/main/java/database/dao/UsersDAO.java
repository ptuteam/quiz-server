package database.dao;

import database.data.UsersDataSet;
import database.executor.TExecutor;
import model.UserProfile;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by dima on 19.11.15.
 */
public class UsersDAO {
    private final Connection connection;

    private static final String TABLE_NAME = "users";

    private static final String COL_ID = "id";
    private static final String COL_FIRST_NAME = "first_name";
    private static final String COL_LAST_NAME = "last_name";
    private static final String COL_EMAIL = "email";
    private static final String COL_AVATAR_URL = "avatarUrl";
    private static final String COL_SCORE = "score";
    private static final String COL_IS_GUEST = "is_guest";

    public UsersDAO(Connection connection) {
        this.connection = connection;
    }

    public UsersDataSet get(int id) throws SQLException {
        String query = "SELECT *" +
                " FROM " + TABLE_NAME +
                " WHERE " + COL_ID + " = " + id + ';';

        TExecutor exec = new TExecutor();
        return exec.execQuery(connection, query, result -> {
            result.next();
            return new UsersDataSet(
                    result.getInt(COL_ID),
                    result.getString(COL_FIRST_NAME),
                    result.getString(COL_LAST_NAME),
                    result.getString(COL_EMAIL),
                    result.getString(COL_AVATAR_URL),
                    result.getInt(COL_SCORE),
                    result.getBoolean(COL_IS_GUEST));
        });
    }

    public UsersDataSet getByEmail(String email) throws SQLException {
        String query = "SELECT *" +
                " FROM " + TABLE_NAME +
                " WHERE " + COL_EMAIL + " = \'" + email + "\';";

        TExecutor exec = new TExecutor();
        return exec.execQuery(connection, query, result -> {
            result.next();
            return new UsersDataSet(
                    result.getInt(COL_ID),
                    result.getString(COL_FIRST_NAME),
                    result.getString(COL_LAST_NAME),
                    result.getString(COL_EMAIL),
                    result.getString(COL_AVATAR_URL),
                    result.getInt(COL_SCORE),
                    result.getBoolean(COL_IS_GUEST));
        });
    }

    public int getUsersCount() throws SQLException {
        final String colCount = "count";
        String query = "SELECT COUNT(*) as " + colCount +
                " FROM " + TABLE_NAME + ';';

        TExecutor exec = new TExecutor();
        return exec.execQuery(connection, query, result -> {
            result.next();
            return result.getInt(colCount);
        });
    }

    public boolean isUserExists(String email) throws SQLException {
        final String colCount = "count";
        String query = "SELECT COUNT(*) as " + colCount +
                " FROM " + TABLE_NAME +
                " WHERE " + COL_EMAIL + " = \'" + email +"\';";

        TExecutor exec = new TExecutor();
        return exec.execQuery(connection, query, result -> {
            result.next();
            return (result.getInt(colCount) > 0);
        });
    }

    public boolean signUpUser(UserProfile user) throws SQLException {
        String query = "INSERT INTO " + TABLE_NAME
                + " (" + COL_FIRST_NAME + ", " + COL_LAST_NAME + ", " + COL_EMAIL + ", " + COL_AVATAR_URL
                + ", " + COL_SCORE + ", " + COL_IS_GUEST + ')'
                + " VALUES (\'" + user.getFirstName() + "\', \'" + user.getLastName() + "\', \'"
                + user.getEmail() + "\', \'" + user.getAvatarUrl()
                + "\', " + user.getScore() + ", " + user.isGuest() + ')';

        TExecutor exec = new TExecutor();
        exec.execQuery(connection, query);
        return true;
    }

    public Collection<UserProfile> getAllUsers() throws SQLException {
        String query = "SELECT * " + " FROM " + TABLE_NAME;

        ArrayList<UserProfile> resultArray = new ArrayList<>();
        TExecutor exec = new TExecutor();
        return exec.execQuery(connection, query, result -> {
            while(result.next())
            {
                UsersDataSet userDataSet =
                        new UsersDataSet(result.getInt(COL_ID), result.getString(COL_FIRST_NAME),
                            result.getString(COL_LAST_NAME), result.getString(COL_EMAIL),
                            result.getString(COL_AVATAR_URL), result.getInt(COL_SCORE),
                            result.getBoolean(COL_IS_GUEST));
                resultArray.add(new UserProfile(userDataSet.getFirstName(), userDataSet.getLastName(),
                        userDataSet.getEmail(), userDataSet.getAvatarUrl(), userDataSet.isGuest()));
            }
            return resultArray;
        });
    }
}
