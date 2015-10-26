package servlets;

import model.UserProfile;
import org.junit.Test;
import utils.AccountService;
import utils.AccountServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static utils.SessionHelper.getNewSession;

/**
 * Created by Dima on 26.10.2015.
 */
public class LogoutServletTest {
    private final static HttpServletRequest request = mock(HttpServletRequest.class);
    private final static HttpServletResponse response = mock(HttpServletResponse.class);
    private final static AccountService accountService = new AccountServiceImpl();

    @Test
    public void testLogout() throws Exception {
        final StringWriter stringWriter = new StringWriter();

        accountService.signIn("session", new UserProfile("first", "last", "email", "avatar"));

        LogoutServlet logoutServlet = new LogoutServlet(accountService);

        when(request.getSession()).thenReturn(getNewSession("session"));
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        logoutServlet.doGet(request, response);

        verify(request, atLeastOnce()).getSession();
        assertTrue(stringWriter.toString().contains("You have been logged out"));
        assertFalse(accountService.isLogged("session"));
    }

    @Test
    public void testNotLoggedLogout() throws Exception {
        final StringWriter stringWriter = new StringWriter();

        accountService.signIn("session2", new UserProfile("first", "last", "email", "avatar"));

        LogoutServlet logoutServlet = new LogoutServlet(accountService);

        when(request.getSession()).thenReturn(getNewSession("session"));
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        logoutServlet.doGet(request, response);

        verify(request, times(1)).getSession();
        assertTrue(stringWriter.toString().contains("You have not been logged"));
    }
}