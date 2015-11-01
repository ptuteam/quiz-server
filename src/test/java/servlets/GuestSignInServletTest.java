package servlets;

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

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Dima on 26.10.2015.
 */
public class GuestSignInServletTest {
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
    }

    @Test
    public void testGuestSignIn() throws ServletException, IOException {
        try (StringWriter stringWriter = new StringWriter()) {
            when(session.getId()).thenReturn("session");
            when(request.getSession()).thenReturn(session);
            when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

            new GuestSignInServlet(accountService).doGet(request, response);

            assertTrue(accountService.isLogged("session"));
            assertTrue(accountService.getUserBySession("session").isGuest());
        }
    }
}