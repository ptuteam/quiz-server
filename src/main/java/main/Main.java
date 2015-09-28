package main;

import model.UserProfile;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.AdministrationServlet;
import servlets.LogoutServlet;
import servlets.SignInServlet;
import servlets.SignUpServlet;
import utils.AccountService;

import javax.servlet.Servlet;


/**
 * alex on 10.09.15.
 */

public class Main {

    public static final int API_VERSION = 1;

    public static void main(String[] args) throws Exception {

        if (args.length != 1) {
            System.out.append("Use port as the first argument");
            System.exit(1);
        }

        String portString = args[0];
        int port = Integer.valueOf(portString);
        System.out.append("Starting at port: ").append(portString).append('\n');

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        AccountService accountService = new AccountService();
        accountService.signUp(new UserProfile("Alexandr", "Udalov", "sashaudalv@gmail.com", "1234", true));

        SignInServlet signInServlet = new SignInServlet(accountService);
        SignUpServlet signUpServlet = new SignUpServlet(accountService);
        LogoutServlet logoutServlet = new LogoutServlet(accountService);
        AdministrationServlet administrationServlet = new AdministrationServlet(accountService);

        context.addServlet(new ServletHolder(signInServlet), SignInServlet.PAGE_URL);
        context.addServlet(new ServletHolder(signUpServlet), SignUpServlet.PAGE_URL);
        context.addServlet(new ServletHolder(logoutServlet), LogoutServlet.PAGE_URL);
        context.addServlet(new ServletHolder(administrationServlet), AdministrationServlet.PAGE_URL);

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setResourceBase("public_html");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler, context});

        Server server = new Server(port);
        server.setHandler(handlers);

        server.start();
        server.join();
    }
}
