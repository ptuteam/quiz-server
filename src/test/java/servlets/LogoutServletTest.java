package servlets;

import model.UserProfile;
import org.junit.Before;
import org.junit.Test;
import utils.AccountService;
import utils.AccountServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Created by Dima on 26.10.2015.
 */
public class LogoutServletTest {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private AccountService accountService;
    private HttpSession session;

    @Before
    public void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        accountService = new AccountServiceImpl();
        session = mock(HttpSession.class);
        accountService.signIn("session", new UserProfile("first", "last", "email", "avatar"));
    }


    @Test
    public void testLogout() throws ServletException, IOException {
        try(final StringWriter stringWriter = new StringWriter()) {
            when(session.getId()).thenReturn("session");
            when(request.getSession()).thenReturn(session);
            when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

            LogoutServlet logoutServlet = new LogoutServlet(accountService);
            logoutServlet.doGet(request, response);

            verify(request, atLeastOnce()).getSession();
            assertFalse(accountService.isLogged("session"));
        }
    }

    @Test
    public void testNotLoggedLogout() throws ServletException, IOException {
        try (final StringWriter stringWriter = new StringWriter()) {
            when(session.getId()).thenReturn("session1");
            when(request.getSession()).thenReturn(session);
            when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

            LogoutServlet logoutServlet = new LogoutServlet(accountService);
            logoutServlet.doGet(request, response);

            verify(request, atLeastOnce()).getSession();
            assertTrue(stringWriter.toString().contains("You have not been logged"));
        }
    }
}