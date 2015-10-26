package servlets;

import org.junit.Test;
import org.mockito.Mockito;
import utils.AccountService;
import utils.AccountServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.*;
import static utils.SessionHelper.getNewSession;

/**
 * Created by Dima on 25.10.2015.
 */
public class SignInServletTest extends Mockito  {
    private final static HttpServletRequest request = mock(HttpServletRequest.class);
    private final static HttpServletResponse response = mock(HttpServletResponse.class);
    private final static AccountService accountService = new AccountServiceImpl();

    @Test
    public void testSignIn() throws Exception {
    }

    @Test
    public void testSignInError() throws Exception {
        StringWriter stringWriter = new StringWriter();

        when(request.getParameter("code")).thenReturn("code");
        when(request.getSession()).thenReturn(getNewSession("session"));
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        SignInServlet signInServlet = new SignInServlet(accountService);
        signInServlet.doGet(request, response);

        verify(request, atLeastOnce()).getParameter("code");
        assertTrue(stringWriter.toString().contains("Sorry, we could not get your profile from google+"));
        assertFalse(accountService.isLogged("session"));
    }
}