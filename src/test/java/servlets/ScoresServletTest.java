package servlets;

import model.UserProfile;
import org.junit.Before;
import org.junit.Test;
import utils.AccountService;
import utils.AccountServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Dima on 26.10.2015.
 */
public class ScoresServletTest {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private AccountService accountService;

    @Before
    public void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        accountService = new AccountServiceImpl();
        accountService.signIn("session", new UserProfile("first", "last", "email", "avatar"));

        UserProfile user1 = new UserProfile("first1", "last1", "email1", "avatar1");
        user1.setScore(1);
        UserProfile user2 = new UserProfile("first2", "last2", "email2", "avatar2");
        user2.setScore(2);

        accountService.signUp(user1);
        accountService.signUp(user2);
    }

    @Test
    public void testScore() throws ServletException, IOException {
        try (StringWriter stringWriter = new StringWriter()) {
            when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

            ScoresServlet scoresServlet = new ScoresServlet(accountService);
            scoresServlet.doGet(request, response);

            assertTrue(stringWriter.toString().contains(
                            "\"first_name\":\"first1\"," +
                                    "\"last_name\":\"last1\"," +
                                    "\"avatar\":\"avatar1\"," +
                                    "\"score\":1")
            );
            assertTrue(stringWriter.toString().contains(
                            "\"first_name\":\"first2\"," +
                                    "\"last_name\":\"last2\"," +
                                    "\"avatar\":\"avatar2\"," +
                                    "\"score\":2")
            );
        }
    }
}