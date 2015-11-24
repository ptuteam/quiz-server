package servlets;

import model.UserProfile;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import utils.AccountService;
import utils.AccountServiceImpl;
import utils.AuthHelper;
import utils.ConfigGeneral;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Dima on 26.10.2015.
 */
@SuppressWarnings("unused")
@RunWith(PowerMockRunner.class)
@PrepareForTest({ AuthHelper.class, ConfigGeneral.class })
public class GuestSignInServletTest {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private AccountService accountService;
    private HttpSession session;

    @SuppressWarnings("Duplicates")
    @Before
    public void setUp() {
        PowerMockito.mockStatic(ConfigGeneral.class);
        when(ConfigGeneral.getDbType()).thenReturn("jdbc:mysql://");
        when(ConfigGeneral.getDbHostName()).thenReturn("localhost:");
        when(ConfigGeneral.getDbPort()).thenReturn("3306/");
        when(ConfigGeneral.getDbNameQuiz()).thenReturn("test_quiz_db?");
        when(ConfigGeneral.getDbNameUsers()).thenReturn("test_quiz_users_db?");
        when(ConfigGeneral.getDbLogin()).thenReturn("user=test_quiz_user&");
        when(ConfigGeneral.getDbPassword()).thenReturn("password=secret");
        ConfigGeneral.loadConfig();

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        accountService = new AccountServiceImpl();
        session = mock(HttpSession.class);
    }

    @Test
    public void testGuestSignIn() throws ServletException, IOException {
        try (StringWriter stringWriter = new StringWriter()) {
            PowerMockito.mockStatic(AuthHelper.class);
            PowerMockito.when(AuthHelper.getGuestUser()).thenReturn(new UserProfile("first11", "last11", "email11", "avatar11", true));
            when(session.getId()).thenReturn("session");
            when(request.getSession()).thenReturn(session);
            when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

            new GuestSignInServlet(accountService).doGet(request, response);

            assertTrue(accountService.isLogged("session"));
            assertTrue(accountService.getUserBySession("session").isGuest());
            assertEquals(accountService.getUserBySession("session").getEmail(), "email11");
        }
    }
}