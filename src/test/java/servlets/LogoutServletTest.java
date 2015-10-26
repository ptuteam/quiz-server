package servlets;

import model.UserProfile;
import org.junit.Assert;
import org.junit.Test;
import utils.AccountService;
import utils.AccountServiceImpl;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import java.io.Console;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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

        when(request.getSession()).thenReturn(getNewSession());
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

        when(request.getSession()).thenReturn(getNewSession());
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        logoutServlet.doGet(request, response);

        verify(request, times(1)).getSession();
        assertTrue(stringWriter.toString().contains("You have not been logged"));
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