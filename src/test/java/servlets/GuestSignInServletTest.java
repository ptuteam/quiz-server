package servlets;

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
 * Created by Dima on 26.10.2015.
 */
public class GuestSignInServletTest {
    private final static HttpServletRequest request = mock(HttpServletRequest.class);
    private final static HttpServletResponse response = mock(HttpServletResponse.class);
    private final static AccountService accountService = new AccountServiceImpl();

    @Test
    public void testGuestSignIn() throws Exception {
        final String sessionId = "session";
        StringWriter stringWriter = new StringWriter();

        when(request.getSession()).thenReturn(SessionHelper.getNewSession(sessionId));
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        new GuestSignInServlet(accountService).doPost(request, response);

        assertTrue(accountService.isLogged(sessionId));
    }
}