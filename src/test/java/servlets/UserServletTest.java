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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Dima on 26.10.2015.
 */
@SuppressWarnings("unused")
@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigGeneral.class})
public class UserServletTest {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private AccountService accountService;
    private HttpSession session;

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

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        accountService = new AccountServiceImpl();
        session = mock(HttpSession.class);
        UserProfile user = new UserProfile("first", "last", "email", "avatar");
        user.setScore(1);
        accountService.signIn("session", user);
    }

    @After
    public void tearDown() throws SQLException {
        String query = "TRUNCATE TABLE users;";

        TExecutor exec = new TExecutor();
        exec.execQuery(DatabaseConnection.getUsersConnection(), query);
    }

    @Test
    public void testUser() throws ServletException, IOException {
        try (StringWriter stringWriter = new StringWriter()) {
            when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
            when(session.getId()).thenReturn("session");
            when(request.getSession()).thenReturn(session);

            UserServlet userServlet = new UserServlet(accountService);
            userServlet.doGet(request, response);

            assertTrue(stringWriter.toString().contains(
                            "\"first_name\":\"first\"," +
                                    "\"last_name\":\"last\"," +
                                    "\"email\":\"email\"," +
                                    "\"avatar\":\"avatar\"," +
                                    "\"score\":1")
            );
        }
    }

    @Test
    public void testUserNotLogged() throws ServletException, IOException {
        try (StringWriter stringWriter = new StringWriter()) {
            when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
            when(session.getId()).thenReturn("session1");
            when(request.getSession()).thenReturn(session);

            UserServlet userServlet = new UserServlet(accountService);
            userServlet.doGet(request, response);

            assertTrue(stringWriter.toString().contains("You have not been logged"));
        }
    }
}