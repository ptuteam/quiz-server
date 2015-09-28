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
public class AdministrationServlet extends HttpServlet {

    public static final String PAGE_URL = "/api/v" + Main.API_VERSION + "/admin";

    private final AccountService accountService;

    public AdministrationServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        String sessionId = request.getSession().getId();
        String msg;

        Map<String, Object> pageVariables = new HashMap<>();

        if (accountService.isLogged(sessionId)) {

            if(accountService.getSession(sessionId).isAdministrator()) {

                pageVariables.put("usersCount", accountService.getUsersCount());
                pageVariables.put("loggedUsersCount", accountService.getLoggedUsersCount());
                response.getWriter().println(PageGenerator.getPage("admin.html", pageVariables));
                response.setStatus(HttpServletResponse.SC_OK);

                String timeString = request.getParameter("shutdown");

                if (timeString != null) {
                    int timeMS = Integer.valueOf(timeString);
                    System.out.print("Server will be down after: "+ timeMS + " ms");

                    try {
                        Thread.sleep(timeMS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.print("\nShutdown");
                    System.exit(0);
                }

            } else {
                msg = "You don't have rights of administrator.";
                pageVariables.put("msg", msg);
                response.getWriter().println(PageGenerator.getPage("accessdenied.html", pageVariables));
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }

        } else {
            msg = "You are not authorised.";
            pageVariables.put("msg", msg);
            response.getWriter().println(PageGenerator.getPage("accessdenied.html", pageVariables));
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }

        response.setContentType("text/html;charset=utf-8");
    }
}
