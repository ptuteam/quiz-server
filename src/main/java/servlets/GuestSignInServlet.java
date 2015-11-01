package servlets;

import main.Main;
import model.UserProfile;
import templater.PageGenerator;
import utils.AccountService;
import utils.AuthHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * alex on 13.10.15.
 */
public class GuestSignInServlet extends HttpServlet {

    public static final String PAGE_URL = "/api/v" + Main.API_VERSION + "/auth/guest";

    private final AccountService accountService;

    public GuestSignInServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        String sessionId = request.getSession().getId();

        if (accountService.isLogged(sessionId)) {
            accountService.logout(sessionId);
        }

        UserProfile guest = AuthHelper.getGuestUser();

        while (!accountService.signUp(guest)) {
            guest = AuthHelper.getGuestUser();
        }

        accountService.signIn(sessionId, guest);

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("authSuccess", "true");

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html");
        response.getWriter().write(PageGenerator.getPage("social_signin_popup.html", pageVariables));
    }
}