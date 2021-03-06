package main;

import database.connection.DatabaseConnection;
import game.RoomManager;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.*;
import utils.AccountService;
import utils.AccountServiceImpl;
import utils.ConfigGeneral;
import websocket.WebSocketGameServlet;
import websocket.WebSocketService;
import websocket.WebSocketServiceImpl;

import java.net.InetSocketAddress;
import java.sql.Connection;


/**
 * alex on 10.09.15.
 */

public class Main {

    public static final int API_VERSION = 1;

    @SuppressWarnings("OverlyBroadThrowsClause")
    public static void main(String[] args) throws Exception {
        ConfigGeneral.loadConfig();

        Connection quizConnection = DatabaseConnection.getQuizConnection();
        Connection usersConnection = DatabaseConnection.getUsersConnection();
        if (quizConnection == null || usersConnection == null) {
            System.exit(1);
        }
        quizConnection.close();
        usersConnection.close();

        System.out.println(String.format("Starting at port: %d\n", ConfigGeneral.getPort()));

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        AccountService accountService = new AccountServiceImpl();
        WebSocketService webSocketService = new WebSocketServiceImpl();
        RoomManager roomManager = new RoomManager(webSocketService);

        SignInServlet signInServlet = new SignInServlet(accountService);
        GuestSignInServlet guestSignInServlet = new GuestSignInServlet(accountService);
        LogoutServlet logoutServlet = new LogoutServlet(accountService);
        AdministrationServlet administrationServlet = new AdministrationServlet(accountService);
        UserServlet userServlet = new UserServlet(accountService);
        ScoresServlet scoresServlet = new ScoresServlet(accountService);
        WebSocketGameServlet webSocketGameServlet = new WebSocketGameServlet(accountService, roomManager);

        context.addServlet(new ServletHolder(signInServlet), SignInServlet.PAGE_URL);
        context.addServlet(new ServletHolder(guestSignInServlet), GuestSignInServlet.PAGE_URL);
        context.addServlet(new ServletHolder(logoutServlet), LogoutServlet.PAGE_URL);
        context.addServlet(new ServletHolder(administrationServlet), AdministrationServlet.PAGE_URL);
        context.addServlet(new ServletHolder(userServlet), UserServlet.PAGE_URL);
        context.addServlet(new ServletHolder(scoresServlet), ScoresServlet.PAGE_URL);
        context.addServlet(new ServletHolder(webSocketGameServlet), WebSocketGameServlet.PAGE_URL);

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setResourceBase("public_html");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler, context});

        Server server = new Server(new InetSocketAddress(ConfigGeneral.getHost(), ConfigGeneral.getPort()));
        server.setHandler(handlers);

        server.start();
        server.join();
    }
}
