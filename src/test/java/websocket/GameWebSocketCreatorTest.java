package websocket;

import game.RoomManager;
import model.UserProfile;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.junit.Before;
import org.junit.Test;
import utils.AccountService;
import utils.AccountServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by dima on 04.11.15.
 */
public class GameWebSocketCreatorTest {
    private AccountService accountService;
    private RoomManager roomManager;

    @Before
    public void setUp() throws Exception {
        accountService = new AccountServiceImpl();
        roomManager = new RoomManager(new WebSocketServiceImpl());

        accountService.signIn("session", new UserProfile("first", "last", "email", "avatar"));
    }

    @Test
    public void testCreateWebSocket() throws Exception {
        ServletUpgradeRequest request = mock(ServletUpgradeRequest.class);
        ServletUpgradeResponse response = mock(ServletUpgradeResponse.class);
        HttpServletRequest httpRequest = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getHttpServletRequest()).thenReturn(httpRequest);
        when(httpRequest.getSession()).thenReturn(session);
        when(session.getId()).thenReturn("session");

        GameWebSocketCreator creator = new GameWebSocketCreator(accountService, roomManager);
        GameWebSocket gameWebSocket = (GameWebSocket)creator.createWebSocket(request, response);

        assertEquals(GameWebSocket.class, gameWebSocket.getClass());
    }

    @Test
    public void testNotLoggedIn() throws Exception {
        ServletUpgradeRequest request = mock(ServletUpgradeRequest.class);
        ServletUpgradeResponse response = mock(ServletUpgradeResponse.class);
        HttpServletRequest httpRequest = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getHttpServletRequest()).thenReturn(httpRequest);
        when(httpRequest.getSession()).thenReturn(session);
        when(session.getId()).thenReturn("session1");

        GameWebSocketCreator creator = new GameWebSocketCreator(accountService, roomManager);

        assertEquals(null, creator.createWebSocket(request, response));
    }
}