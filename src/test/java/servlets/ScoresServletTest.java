package servlets;

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
import utils.AccountService;
import utils.AccountServiceImpl;
import utils.ConfigGeneral;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Dima on 26.10.2015.
 */
@SuppressWarnings("unused")
@RunWith(PowerMockRunner.class)
@PrepareForTest({ScoresServlet.class, ConfigGeneral.class})
public class ScoresServletTest {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private AccountService accountService;

    @Before
    public void setUp() {
        PowerMockito.mockStatic(ConfigGeneral.class);
        when(ConfigGeneral.getRatingUsersCount()).thenReturn(3);
        when(ConfigGeneral.getDbType()).thenReturn("jdbc:mysql://");
        when(ConfigGeneral.getDbHostName()).thenReturn("localhost:");
        when(ConfigGeneral.getDbPort()).thenReturn("3306/");
        when(ConfigGeneral.getDbNameQuiz()).thenReturn("test_quiz_db?");
        when(ConfigGeneral.getDbNameUsers()).thenReturn("test_quiz_users_db?");
        when(ConfigGeneral.getDbLogin()).thenReturn("user=test_quiz_user&");
        when(ConfigGeneral.getDbPassword()).thenReturn("password=secret");
        ConfigGeneral.loadConfig();

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        accountService = new AccountServiceImpl();
        accountService.signIn("session", new UserProfile("first", "last", "email", "avatar"));

        UserProfile user1 = new UserProfile("first1", "last1", "email1", "avatar1");
        user1.setScore(1);
        UserProfile user2 = new UserProfile("first2", "last2", "email2", "avatar2");
        user2.setScore(2);
        UserProfile user3 = new UserProfile("first3", "last3", "email3", "avatar3");
        user3.setScore(0);
        UserProfile user4 = new UserProfile("first4", "last4", "email4", "avatar4");
        user4.setScore(3);
        UserProfile user5 = new UserProfile("first5", "last5", "email5", "avatar5");
        user5.setScore(4);

        accountService.signUp(user1);
        accountService.signUp(user2);
        accountService.signUp(user3);
        accountService.signUp(user4);
        accountService.signUp(user5);
    }

    @After
    public void tearDown() throws SQLException {
        String query = "TRUNCATE TABLE users;";

        TExecutor.execQuery(DatabaseConnection.getUsersConnection(), query);
    }

    @Test
    public void testScore() throws ServletException, IOException {
        try (StringWriter stringWriter = new StringWriter()) {
            when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

            ScoresServlet scoresServlet = new ScoresServlet(accountService);
            scoresServlet.doGet(request, response);

            assertTrue(stringWriter.toString().contains(
                            "\"first_name\":\"first4\"," +
                                    "\"last_name\":\"last4\"," +
                                    "\"score\":3")
            );
            assertTrue(stringWriter.toString().contains(
                            "\"first_name\":\"first5\"," +
                                    "\"last_name\":\"last5\"," +
                                    "\"score\":4")
            );
            assertTrue(stringWriter.toString().contains(
                            "\"first_name\":\"first2\"," +
                                    "\"last_name\":\"last2\"," +
                                    "\"score\":2")
            );
            assertFalse(stringWriter.toString().contains(
                            "\"first_name\":\"first1\"," +
                                    "\"last_name\":\"last1\"," +
                                    "\"score\":1")
            );
            assertFalse(stringWriter.toString().contains(
                            "\"first_name\":\"first3\"," +
                                    "\"last_name\":\"last3\"," +
                                    "\"score\":0")
            );
        }
    }
}