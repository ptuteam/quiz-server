package websocket;

import game.RoomManager;
import model.UserProfile;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import utils.AccountService;

/**
 * alex on 25.10.15.
 */
public class GameWebSocketCreator implements WebSocketCreator {
    private final AccountService accountService;
    private final RoomManager roomManager;

    public GameWebSocketCreator(AccountService accountService, RoomManager roomManager) {
        this.accountService = accountService;
        this.roomManager = roomManager;
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest request, ServletUpgradeResponse response) {
        String sessionId = request.getHttpServletRequest().getSession().getId();
        if (accountService.isLogged(sessionId)) {
            UserProfile user = accountService.getUserBySession(sessionId);
            String mode = request.getHttpServletRequest().getParameter("mode");
            String roomIdStr = request.getHttpServletRequest().getParameter("roomId");
            String typeStr = request.getHttpServletRequest().getParameter("type");
            long roomId = 0;
            if (roomIdStr != null) {
                try {
                    roomId = Long.valueOf(roomIdStr);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            int type = 0;
            if (typeStr != null) {
                try {
                    type = Integer.valueOf(typeStr);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            return new GameWebSocket(user, roomManager, mode, roomId, type);
        }
        return null;
    }
}
