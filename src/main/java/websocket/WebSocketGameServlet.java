package websocket;

import game.RoomManager;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import utils.AccountService;

import javax.servlet.annotation.WebServlet;

/**
 * alex on 25.10.15.
 */
@WebServlet(name = "WebSocketGameServlet", urlPatterns = {"/gameplay"})
public class WebSocketGameServlet extends WebSocketServlet {
    public static final String PAGE_URL = "/gameplay";

    private static final int IDLE_TIME = 5 * 60 * 1000;
    private final AccountService accountService;
    private final RoomManager roomManager;

    public WebSocketGameServlet(AccountService accountService,
                                RoomManager roomManager) {
        this.accountService = accountService;
        this.roomManager = roomManager;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(IDLE_TIME);
        factory.setCreator(new GameWebSocketCreator(accountService, roomManager));
    }
}
