package servlets;

import model.UserProfile;
import org.junit.AfterClass;
import org.junit.Test;
import utils.AccountService;
import utils.AccountServiceImpl;
import utils.SessionHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by dima on 01.11.15.
 */
public class AdministrationServletTest {
    private final static HttpServletRequest request = mock(HttpServletRequest.class);
    private final static HttpServletResponse response = mock(HttpServletResponse.class);
    private final static AccountService accountService = new AccountServiceImpl();

    @Test
    public void testNotLoggedIn() throws Exception {
        StringWriter stringWriter = new StringWriter();

        accountService.signIn("session", new UserProfile("admin", "admin", "sashaudalv@gmail.com", "avatar"));

        when(request.getSession()).thenReturn(SessionHelper.getNewSession("session1"));
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        AdministrationServlet administrationServlet = new AdministrationServlet(accountService);
        administrationServlet.doGet(request, response);

        assertTrue(stringWriter.toString().contains("You are not authorised."));
    }

    @Test
    public void testWithoutParameter() throws Exception {
        StringWriter stringWriter = new StringWriter();

        accountService.signIn("session", new UserProfile("admin", "admin", "sashaudalv@gmail.com", "avatar"));

        when(request.getSession()).thenReturn(SessionHelper.getNewSession("session"));
        when(request.getParameter("shutdown")).thenReturn(null);
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        AdministrationServlet administrationServlet = new AdministrationServlet(accountService);
        administrationServlet.doGet(request, response);

        assertTrue(stringWriter.toString().contains("Users: 0"));
        assertTrue(stringWriter.toString().contains("Logged users: 1"));
    }

    @Test
    public void testNotAdministration() throws Exception {
        StringWriter stringWriter = new StringWriter();

        accountService.signIn("session", new UserProfile("admin", "admin", "admin", "avatar"));

        when(request.getSession()).thenReturn(SessionHelper.getNewSession("session"));
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        AdministrationServlet administrationServlet = new AdministrationServlet(accountService);
        administrationServlet.doGet(request, response);

        assertTrue(stringWriter.toString().contains("You don't have rights of administrator."));

    }

    @Test
    public void testAdministration() throws Exception {
        StringWriter stringWriter = new StringWriter();

        accountService.signIn("session", new UserProfile("admin", "admin", "sashaudalv@gmail.com", "avatar"));

        when(request.getSession()).thenReturn(SessionHelper.getNewSession("session"));
        when(request.getParameter("shutdown")).thenReturn("5000");
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        AdministrationServlet administrationServlet = new AdministrationServlet(accountService);
        administrationServlet.doGet(request, response);
    }
}