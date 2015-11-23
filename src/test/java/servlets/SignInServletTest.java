package servlets;

import com.mashape.unirest.http.exceptions.UnirestException;
import model.UserProfile;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import templater.PageGenerator;
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
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by Dima on 25.10.2015.
 */
@SuppressWarnings("unused")
@RunWith(PowerMockRunner.class)
@PrepareForTest({ AuthHelper.class, ConfigGeneral.class})
public class SignInServletTest extends Mockito {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private AccountService accountService;
    private HttpSession session;

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
    public void testSignIn() throws ServletException, UnirestException, IOException {
        try (StringWriter stringWriter = new StringWriter()) {
            PowerMockito.mockStatic(AuthHelper.class);
            PowerMockito.when(AuthHelper.getUserFromSocial(anyString())).thenReturn(new UserProfile("first", "last", "email", "avatar"));
            when(request.getParameter("code")).thenReturn("code");
            when(session.getId()).thenReturn("session");
            when(request.getSession()).thenReturn(session);
            when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

            SignInServlet signInServlet = new SignInServlet(accountService);
            signInServlet.doGet(request, response);

            Map<String, Object> variables = new HashMap<>();
            variables.put("authSuccess", "true");

            verify(request, atLeastOnce()).getParameter("code");
            assertEquals(stringWriter.toString(), PageGenerator.getPage("social_signin_popup.html", variables));
            assertEquals(accountService.getUserBySession("session").getEmail(), "email");
        }
    }

    @Test
    public void testSignInError() throws ServletException, IOException, UnirestException {
        try (StringWriter stringWriter = new StringWriter()) {
            PowerMockito.mockStatic(AuthHelper.class);
            PowerMockito.when(AuthHelper.getUserFromSocial(anyString())).thenReturn(null);
            when(request.getParameter("code")).thenReturn("code");
            when(session.getId()).thenReturn("session");
            when(request.getSession()).thenReturn(session);
            when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

            SignInServlet signInServlet = new SignInServlet(accountService);
            signInServlet.doGet(request, response);

            Map<String, Object> variables = new HashMap<>();
            variables.put("authSuccess", "false");

            verify(request, atLeastOnce()).getParameter("code");
            assertEquals(stringWriter.toString(), PageGenerator.getPage("social_signin_popup.html", variables));
            assertFalse(accountService.isLogged("session"));
        }
    }

    @Test
    public void testSignInWithoutParameter() throws ServletException, IOException {
        SignInServlet signInServlet = new SignInServlet(accountService);
        signInServlet.doGet(request, response);

        verify(request, atLeastOnce()).getParameter("code");
        verify(response, atLeastOnce()).sendRedirect(anyString());
    }
}