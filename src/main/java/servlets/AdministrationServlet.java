package servlets;

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

    public static final String PAGE_URL = "/admin";

    private final AccountService accountService;

    public AdministrationServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        String sessionId = request.getSession().getId();
        String templateFile;

        Map<String, Object> pageVariables = new HashMap<>();

        if (accountService.isLogged(sessionId)) {

            if (accountService.getUserBySession(sessionId).isAdministrator()) {

                String timeString = request.getParameter("shutdown");

                if (timeString != null) {

                    int timeMS = 0;

                    try {
                        timeMS = Integer.parseInt(timeString);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                    System.out.print("Server will be down after: " + timeMS + " ms\n");

                    if (timeMS > 0) {
                        try {
                            Thread.sleep(timeMS);
                        } catch (IllegalArgumentException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    System.out.print("Shutdown");
                    System.exit(0);
                }

                pageVariables.put("usersCount", accountService.getUsersCount());
                pageVariables.put("loggedUsersCount", accountService.getLoggedUsersCount());
                templateFile = "admin.html";
                response.setStatus(HttpServletResponse.SC_OK);

            } else {
                pageVariables.put("msg", "You don't have rights of administrator.");
                templateFile = "accessdenied.html";
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }

        } else {
            pageVariables.put("msg", "You are not authorised.");
            templateFile = "accessdenied.html";
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }

        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(PageGenerator.getPage(templateFile, pageVariables));
    }
}
