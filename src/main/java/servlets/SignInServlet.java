package servlets;

import main.Main;
import model.UserProfile;
import templater.PageGenerator;
import utils.AccountService;

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

    AccountService accountService;

    public SignInServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        String sessionId = request.getSession().getId();
        String msg = "";

        Map < String, Object > pageVariables = new HashMap<>();
        pageVariables.put("url", PAGE_URL);

        if (accountService.isLogged(sessionId)) {
            msg = "You are already logged.";
        }

        pageVariables.put("msg", msg);

        response.getWriter().println(PageGenerator.getPage("signin.html", pageVariables));
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        String sessionId = request.getSession().getId();

        UserProfile user;

        if (accountService.getUser(email) != null) {

            user = accountService.getUser(email);

            if (user.getPassword().equals(password)) {
                accountService.signIn(sessionId, user);
                pageVariables.put("firstName", user.getFirstName());
                pageVariables.put("lastName", user.getLastName());
                pageVariables.put("email", user.getEmail());
                response.getWriter().println(PageGenerator.getPage("signinresponse.txt", pageVariables));
                response.setStatus(HttpServletResponse.SC_OK);

            } else {

                pageVariables.put("code", 401);
                pageVariables.put("description", "The pair login - password is wrong.");
                response.getWriter().println(PageGenerator.getPage("errorresponse.txt", pageVariables));
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            }

        } else {

            pageVariables.put("code", 401);
            pageVariables.put("description", "There is no user with this email.");
            response.getWriter().println(PageGenerator.getPage("errorresponse.txt", pageVariables));
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        }

        response.setContentType("application/json; charset=utf-8");
    }
}
