package servlets;

import com.mashape.unirest.http.exceptions.UnirestException;
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
 * alex on 25.09.15.
 */
public class SignInServlet extends HttpServlet {

    public static final String PAGE_URL = "/api/v" + Main.API_VERSION + "/auth/signin";

    private final AccountService accountService;

    public SignInServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        String code = request.getParameter("code");

        if (code == null) {

            response.sendRedirect("/#login");

        } else {

            String sessionId = request.getSession().getId();

            if (accountService.isLogged(sessionId)) {
                accountService.logout(sessionId);
            }

            UserProfile user = null;

            try {
                user = AuthHelper.getUserFromSocial(code);
            } catch (UnirestException | NullPointerException e) {
                e.printStackTrace();
            }

            Map<String, Object> pageVariables = new HashMap<>();

            if (user != null) {

                if (accountService.isUserExist(user.getEmail())) {
                    user = accountService.getUser(user.getEmail());
                } else {
                    accountService.signUp(user);
                }

                accountService.signIn(sessionId, user);

                pageVariables.put("authSuccess", "true");

            } else {
                pageVariables.put("authSuccess", "false");
            }

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/html");
            response.getWriter().write(PageGenerator.getPage("social_signin_popup.html", pageVariables));
        }
    }
}
