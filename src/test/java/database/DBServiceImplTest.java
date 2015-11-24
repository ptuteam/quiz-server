package database;

import database.connection.DatabaseConnection;
import database.executor.TExecutor;
import model.UserProfile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import utils.ConfigGeneral;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by dima on 24.11.15.
 */
@SuppressWarnings("unused")
@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigGeneral.class})
public class DBServiceImplTest {

    private final DBService dbService = new DBServiceImpl();
    private final UserProfile user = new UserProfile("first", "last", "email", "avatar");
    private final UserProfile user2 = new UserProfile("first2", "last2", "email2", "avatar2");

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
        ConfigGeneral.loadConfig();

        dbService.signUpUser(user);
        dbService.signUpUser(user2);
    }

    @After
    public void tearDown() throws SQLException {
        String query = "TRUNCATE TABLE users;";

        TExecutor exec = new TExecutor();
        exec.execQuery(DatabaseConnection.getUsersConnection(), query);
    }

    @Test
    public void testGetUserByEmail() throws Exception {
        assertEquals(user.getEmail(), dbService.getUserByEmail(user.getEmail()).getEmail());
        assertEquals(user2.getEmail(), dbService.getUserByEmail(user2.getEmail()).getEmail());
    }

    @Test
    public void testIsUserExist() throws Exception {
        assertTrue(dbService.isUserExist(user.getEmail()));
        assertTrue(dbService.isUserExist(user2.getEmail()));
        assertFalse(dbService.isUserExist("qwerty"));
    }

    @Test
    public void testGetUsersCount() throws Exception {
        assertEquals(2, dbService.getUsersCount());
    }

    @Test
    public void testGetAllUsers() throws Exception {
        Iterator<UserProfile> iterator = dbService.getAllUsers().iterator();
        assertEquals("email", iterator.next().getEmail());
        assertEquals("email2", iterator.next().getEmail());
    }

    @Test
    public void testUpdateUserScore() throws Exception {
        dbService.updateUserScore(user.getEmail(), 10);
        assertEquals(10, dbService.getUserByEmail(user.getEmail()).getScore());
    }

    @Test
    public void testGetRandomQuestion() throws Exception {
        assertNotNull(dbService.getRandomQuestion(1, new HashSet<>()));
    }
}