package servlets;

import database.DBServiceImpl;
import model.UserProfile;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
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
import java.io.*;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Created by dima on 01.11.15.
 */
@SuppressWarnings("unused")
@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigGeneral.class})
public class AdministrationServletTest {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private AccountService accountService;
    private HttpSession session;

    @SuppressWarnings("PublicField")
    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

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
        session = mock(HttpSession.class);
        accountService = new AccountServiceImpl();
        accountService.signUp(new UserProfile("admin", "admin", "sashaudalv@gmail.com", "avatar"));
        accountService.signIn("session", new UserProfile("admin", "admin", "sashaudalv@gmail.com", "avatar"));
    }

    @Test
    public void testNotLoggedIn() throws ServletException, IOException {
        try (StringWriter stringWriter = new StringWriter()) {
            when(session.getId()).thenReturn("session1");
            when(request.getSession()).thenReturn(session);
            when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

            AdministrationServlet administrationServlet = new AdministrationServlet(accountService);
            administrationServlet.doGet(request, response);

            assertTrue(stringWriter.toString().contains("You are not authorised."));
        }
    }

    @Test
    public void testAdministratorPage() throws ServletException, IOException {
        try (StringWriter stringWriter = new StringWriter()) {
            when(session.getId()).thenReturn("session");
            when(request.getSession()).thenReturn(session);
            when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

            AdministrationServlet administrationServlet = new AdministrationServlet(accountService);
            administrationServlet.doGet(request, response);

            assertTrue(stringWriter.toString().contains("Users: "
                    + new DBServiceImpl().getUsersCount()));
            assertTrue(stringWriter.toString().contains("Logged users: 1"));
        }
    }

    @Test
    public void testNotAdministrator() throws ServletException, IOException {
        try (StringWriter stringWriter = new StringWriter()) {
            accountService.signIn("session1", new UserProfile("notadmin", "notadmin", "notadmin", "avatar"));

            when(session.getId()).thenReturn("session1");
            when(request.getSession()).thenReturn(session);
            when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

            AdministrationServlet administrationServlet = new AdministrationServlet(accountService);
            administrationServlet.doGet(request, response);

            assertTrue(stringWriter.toString().contains("You don't have rights of administrator."));
        }
    }

    @Test
    public void testServerShutdown() throws ServletException, IOException {
        try (StringWriter stringWriter = new StringWriter()) {
            exit.expectSystemExitWithStatus(0);
            when(session.getId()).thenReturn("session");
            when(request.getSession()).thenReturn(session);
            when(request.getParameter("shutdown")).thenReturn("0");
            when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

            AdministrationServlet administrationServlet = new AdministrationServlet(accountService);
            administrationServlet.doGet(request, response);
        }
    }
}