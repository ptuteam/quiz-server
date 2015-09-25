package servlets;

import main.Main;
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
public class LogoutServlet extends HttpServlet {

    public static final String PAGE_URL = "/api/v" + Main.API_VERSION + "/logout";

    AccountService accountService;

    public LogoutServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        String sessionId = request.getSession().getId();

        if (accountService.isLogged(sessionId)) {

            accountService.logout(sessionId);
            response.getWriter().println(PageGenerator.getPage("logoutresponse.txt", pageVariables));

        } else {

            pageVariables.put("code", 403);
            pageVariables.put("description", "You have not been logged");
            response.getWriter().println(PageGenerator.getPage("errorresponse.txt", pageVariables));

        }

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
