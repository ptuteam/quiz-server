package servlets;

import org.junit.Test;
import org.mockito.Mockito;
import utils.AccountService;
import utils.AccountServiceImpl;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;

import static org.junit.Assert.*;

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
        when(request.getSession()).thenReturn(getNewSession());
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        SignInServlet signInServlet = new SignInServlet(accountService);
        signInServlet.doGet(request, response);

        verify(request, atLeastOnce()).getParameter("code");
        assertTrue(stringWriter.toString().contains("Sorry, we could not get your profile from google+"));
        assertFalse(accountService.isLogged("session"));
    }

    // return HttpSession with Id="session"
    private HttpSession getNewSession()
    {

        return new HttpSession() {
            @Override
            public long getCreationTime() {
                return 0;
            }

            @Override
            public String getId() {
                return "session";
            }

            @Override
            public long getLastAccessedTime() {
                return 0;
            }

            @Override
            public ServletContext getServletContext() {
                return null;
            }

            @Override
            public void setMaxInactiveInterval(int i) {

            }

            @Override
            public int getMaxInactiveInterval() {
                return 0;
            }

            @Override
            public HttpSessionContext getSessionContext() {
                return null;
            }

            @Override
            public Object getAttribute(String s) {
                return null;
            }

            @Override
            public Object getValue(String s) {
                return null;
            }

            @Override
            public Enumeration<String> getAttributeNames() {
                return null;
            }

            @Override
            public String[] getValueNames() {
                return new String[0];
            }

            @Override
            public void setAttribute(String s, Object o) {

            }

            @Override
            public void putValue(String s, Object o) {

            }

            @Override
            public void removeAttribute(String s) {

            }

            @Override
            public void removeValue(String s) {

            }

            @Override
            public void invalidate() {

            }

            @Override
            public boolean isNew() {
                return false;
            }
        };
    }
}