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

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Dima on 26.10.2015.
 */
@SuppressWarnings("unused")
public class UserServletTest {
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
        UserProfile user = new UserProfile("first", "last", "email", "avatar");
        user.setScore(1);
        accountService.signIn("session", user);
    }

    @Test
    public void testUser() throws ServletException, IOException {
        try (StringWriter stringWriter = new StringWriter()) {
            when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
            when(session.getId()).thenReturn("session");
            when(request.getSession()).thenReturn(session);

            UserServlet userServlet = new UserServlet(accountService);
            userServlet.doGet(request, response);

            assertTrue(stringWriter.toString().contains(
                            "\"first_name\":\"first\"," +
                                    "\"last_name\":\"last\"," +
                                    "\"email\":\"email\"," +
                                    "\"avatar\":\"avatar\"," +
                                    "\"score\":1")
            );
        }
    }

    @Test
    public void testUserNotLogged() throws ServletException, IOException {
        try (StringWriter stringWriter = new StringWriter()) {
            when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
            when(session.getId()).thenReturn("session1");
            when(request.getSession()).thenReturn(session);

            UserServlet userServlet = new UserServlet(accountService);
            userServlet.doGet(request, response);

            assertTrue(stringWriter.toString().contains("You have not been logged"));
        }
    }
}