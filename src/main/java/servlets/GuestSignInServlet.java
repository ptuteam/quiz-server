package servlets;

import com.google.gson.JsonObject;
import main.Main;
import model.UserProfile;
import utils.AccountService;
import utils.AuthHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
    public void doPost(HttpServletRequest request,
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

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("first_name", guest.getFirstName());
        jsonObject.addProperty("last_name", guest.getLastName());
        jsonObject.addProperty("email", guest.getEmail());
        jsonObject.addProperty("avatar", guest.getAvatarUrl());
        jsonObject.addProperty("score", guest.getScore());

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(jsonObject);
    }
}