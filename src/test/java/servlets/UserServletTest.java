package servlets;

import model.UserProfile;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.AccountService;
import utils.AccountServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static utils.SessionHelper.getNewSession;

/**
 * Created by Dima on 26.10.2015.
 */
public class UserServletTest {
    private final static HttpServletRequest request = mock(HttpServletRequest.class);
    private final static HttpServletResponse response = mock(HttpServletResponse.class);
    private final static AccountService accountService = new AccountServiceImpl();

    @BeforeClass
    public static void setUp() throws Exception {
        UserProfile user = new UserProfile("first", "last", "email", "avatar");
        user.setScore(1);
        accountService.signIn("session", user);
    }

    @Test
    public void testUser() throws Exception {
        StringWriter stringWriter = new StringWriter();

        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        when(request.getSession()).thenReturn(getNewSession("session"));

        new UserServlet(accountService).doGet(request, response);

        assertTrue(stringWriter.toString().contains(
                "\"first_name\":\"first\"," +
                "\"last_name\":\"last\"," +
                "\"email\":\"email\"," +
                "\"avatar\":\"avatar\"," +
                "\"score\":1"));
    }

    @Test
    public void testUserNotLogged() throws Exception {
        StringWriter stringWriter = new StringWriter();

        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        when(request.getSession()).thenReturn(getNewSession("session1"));

        new UserServlet(accountService).doGet(request, response);

        assertTrue(stringWriter.toString().contains("You have not been logged"));
    }
}