package servlets;

import com.google.gson.JsonObject;
import main.Main;
import utils.AccountService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * alex on 25.09.15.
 */
public class LogoutServlet extends HttpServlet {

    public static final String PAGE_URL = "/api/v" + Main.API_VERSION + "/user/logout";

    private final AccountService accountService;

    public LogoutServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        String sessionId = request.getSession().getId();
        JsonObject jsonObject = new JsonObject();

        if (accountService.isLogged(sessionId)) {

            accountService.logout(sessionId);

            jsonObject.addProperty("code", HttpServletResponse.SC_OK);
            jsonObject.addProperty("description", "You have been logged out");

            response.setStatus(HttpServletResponse.SC_OK);

        } else {

            jsonObject.addProperty("code", HttpServletResponse.SC_FORBIDDEN);
            jsonObject.addProperty("description", "You have not been logged");

            response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        }

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(jsonObject.toString());
    }
}
