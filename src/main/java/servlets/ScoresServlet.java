package servlets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import main.Main;
import model.UserProfile;
import utils.AccountService;
import utils.ConfigGeneral;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * alex on 23.10.15.
 */
public class ScoresServlet extends HttpServlet {

    public static final String PAGE_URL = "/api/v" + Main.API_VERSION + "/scores";

    private final AccountService accountService;

    public ScoresServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        List<UserProfile> users = new ArrayList<>(accountService.getTopUsers(ConfigGeneral.getRatingUsersCount()));

        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        jsonObject.add("users", jsonArray);

        users.stream().filter(user -> user.getScore() > 0).forEach(user -> {
            JsonObject userJsonObject = new JsonObject();
            userJsonObject.addProperty("first_name", user.getFirstName());
            userJsonObject.addProperty("last_name", user.getLastName());
            userJsonObject.addProperty("score", user.getScore());
            jsonArray.add(userJsonObject);
        });

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(jsonObject);
    }
}
