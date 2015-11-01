package servlets;

import model.UserProfile;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import utils.AccountService;
import utils.AccountServiceImpl;

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
public class AdministrationServletTest {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private AccountService accountService;
    private HttpSession session;

    @Rule
    private final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Before
    public void setUp() {
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

            assertTrue(stringWriter.toString().contains("Users: 1"));
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