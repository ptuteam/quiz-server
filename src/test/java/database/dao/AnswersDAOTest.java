package database.dao;

import database.connection.DatabaseConnection;
import database.data.AnswersDataSet;
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
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by dima on 24.11.15.
 */
@SuppressWarnings("unused")
@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigGeneral.class})
public class AnswersDAOTest {

    private AnswersDAO answersDAO;
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
        answersDAO = new AnswersDAO(connection);
    }

    @After
    public void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    public void testGet() throws SQLException {
        assertEquals("answer 1, question 1", answersDAO.get(1).getText());
    }

    @Test
    public void testGetByQuestionId() throws SQLException {
        List<AnswersDataSet> answers = answersDAO.getByQuestionId(1);
        assertEquals("answer 1, question 1", answers.get(0).getText());
        assertEquals("answer 2, question 1", answers.get(1).getText());
        assertEquals("answer 3, question 1", answers.get(2).getText());
        assertEquals("answer 4, question 1", answers.get(3).getText());

        answers = answersDAO.getByQuestionId(2);
        assertEquals("answer, question 2", answers.get(0).getText());
    }

    @Test
    public void testGetCorrectByQuestionId() throws SQLException {
        assertEquals("answer 1, question 1", answersDAO.getCorrectByQuestionId(1).getText());
        assertEquals("answer, question 2", answersDAO.getCorrectByQuestionId(2).getText());
    }
}