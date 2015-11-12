package servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import main.Main;
import model.UserProfile;
import model.UserProfileSerialiser;
import utils.AccountService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * alex on 21.10.15.
 */
public class UserServlet extends HttpServlet {

    public static final String PAGE_URL = "/api/v" + Main.API_VERSION + "/user/get";

    private final AccountService accountService;

    public UserServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        JsonObject jsonObject = new JsonObject();
        String sessionId = request.getSession().getId();

        if (accountService.isLogged(sessionId)) {

            UserProfile user = accountService.getUserBySession(sessionId);

            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(UserProfile.class, new UserProfileSerialiser());
            Gson gson = builder.create();

            jsonObject.addProperty("code", HttpServletResponse.SC_OK);
            jsonObject.add("user", gson.toJsonTree(user));

            response.setStatus(HttpServletResponse.SC_OK);

        } else {

            jsonObject.addProperty("code", HttpServletResponse.SC_FORBIDDEN);
            jsonObject.addProperty("description", "You have not been logged");

            response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        }

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(jsonObject);
    }
}
