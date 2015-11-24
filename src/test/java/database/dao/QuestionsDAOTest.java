package database.dao;

import database.connection.DatabaseConnection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import utils.ConfigGeneral;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by dima on 24.11.15.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigGeneral.class})
@SuppressWarnings("unused")
public class QuestionsDAOTest {

    private QuestionsDAO questionsDAO;
    private Connection connection;

    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(ConfigGeneral.class);
        when(ConfigGeneral.getDbType()).thenReturn("jdbc:mysql://");
        when(ConfigGeneral.getDbHostName()).thenReturn("localhost:");
        when(ConfigGeneral.getDbPort()).thenReturn("3306/");
        when(ConfigGeneral.getDbNameQuiz()).thenReturn("test_quiz_db?");
        when(ConfigGeneral.getDbNameUsers()).thenReturn("test_quiz_users_db?");
        when(ConfigGeneral.getDbLogin()).thenReturn("user=test_quiz_user&");
        when(ConfigGeneral.getDbPassword()).thenReturn("password=secret");
        ConfigGeneral.loadConfig();

        connection = DatabaseConnection.getQuizConnection();
        questionsDAO = new QuestionsDAO(connection);
    }

    @After
    public void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    public void testGet() throws SQLException {
        assertEquals("question1", questionsDAO.get(1).getTitle());
    }

    @Test
    public void testGetRandom() throws SQLException {
        Set<Integer> askedQuestions = new HashSet<>();
        askedQuestions.add(2);
        assertEquals("question1", questionsDAO.getRandom(1, askedQuestions).getTitle());
    }
}