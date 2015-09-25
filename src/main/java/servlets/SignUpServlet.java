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
public class SignUpServlet extends HttpServlet {

    public static final String PAGE_URL = "/api/v" + Main.API_VERSION + "/auth/signup";

    AccountService accountService;

    public SignUpServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("url", PAGE_URL);

        response.getWriter().println(PageGenerator.getPage("signup.html", pageVariables));
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();

        String firstName = request.getParameter("firstname");
        String lastName = request.getParameter("lastname");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (accountService.signUp(new UserProfile(firstName, lastName, email, password))) {
            pageVariables.put("firstName", firstName);
            pageVariables.put("lastName", lastName);
            pageVariables.put("email", email);
            pageVariables.put("password", password);

            response.getWriter().println(PageGenerator.getPage("signupresponse.txt", pageVariables));
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            pageVariables.put("code", 403);
            pageVariables.put("description", "User with same email already exists.");

            response.getWriter().println(PageGenerator.getPage("errorresponse.txt", pageVariables));
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }

        response.setContentType("application/json; charset=utf-8");
    }
}
