package database.connection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import utils.ConfigGeneral;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Created by dima on 23.11.15.
 */
@SuppressWarnings("unused")
@RunWith(PowerMockRunner.class)
@PrepareForTest({DatabaseConnection.class, ConfigGeneral.class})
public class DatabaseConnectionTest {

    @Before
    public void setUp() {
        PowerMockito.mockStatic(ConfigGeneral.class);
        when(ConfigGeneral.getDbType()).thenReturn("jdbc:mysql://");
        when(ConfigGeneral.getDbHostName()).thenReturn("localhost:");
        when(ConfigGeneral.getDbPort()).thenReturn("3306/");
        when(ConfigGeneral.getDbNameQuiz()).thenReturn("test_quiz_db?");
        when(ConfigGeneral.getDbNameUsers()).thenReturn("test_quiz_users_db?");
        when(ConfigGeneral.getDbLogin()).thenReturn("user=test_quiz_user&");
        when(ConfigGeneral.getDbPassword()).thenReturn("password=secret");
    }

    @Test
    public void testGetQuizConnection() throws SQLException {
        Connection connection = DatabaseConnection.getQuizConnection();

        if (connection == null) {
            System.out.println("Please install test database by executing quiz_db_scheme.sql" +
                    " from root in your mysql server.");
        } else {
            assertEquals("test_quiz_user@localhost", connection.getMetaData().getUserName());
            assertEquals("test_quiz_db", connection.getCatalog());
        }
    }

    @Test
    public void testGetUsersConnection() throws SQLException {
        Connection connection = DatabaseConnection.getUsersConnection();

        if (connection == null) {
            System.out.println("Please install test database by executing quiz_db_scheme.sql" +
                    " from root in your mysql server.");
        } else {
            assertEquals("test_quiz_user@localhost", connection.getMetaData().getUserName());
            assertEquals("test_quiz_users_db", connection.getCatalog());
        }
    }

    @Test
    public void testGetConnectionWrongDB() throws SQLException {
        when(ConfigGeneral.getDbNameUsers()).thenReturn("test_quiz_users_db1?");

        Connection connection = DatabaseConnection.getUsersConnection();

        assertTrue(connection == null);
    }
}