package websocket;

import game.RoomManager;
import model.UserProfile;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import utils.AccountService;
import utils.AccountServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

/**
 * Created by dima on 04.11.15.
 */
@SuppressWarnings("unused")
@RunWith(MockitoJUnitRunner.class)
public class GameWebSocketCreatorTest {

    @Mock
    private ServletUpgradeRequest request;

    @Mock
    private ServletUpgradeResponse response;

    @Mock
    private HttpServletRequest httpRequest;

    @Mock
    private HttpSession session;

    private AccountService accountService;
    private RoomManager roomManager;

    @Before
    public void setUp() {
        accountService = new AccountServiceImpl();
        roomManager = new RoomManager(new WebSocketServiceImpl());
        when(request.getHttpServletRequest()).thenReturn(httpRequest);
        when(httpRequest.getSession()).thenReturn(session);

        accountService.signIn("session", new UserProfile("first", "last", "email", "avatar"));
    }

    @Test
    public void testCreateWebSocket() throws Exception {
        when(session.getId()).thenReturn("session");

        GameWebSocketCreator creator = new GameWebSocketCreator(accountService, roomManager);
        GameWebSocket gameWebSocket = (GameWebSocket) creator.createWebSocket(request, response);

        assertNotNull(gameWebSocket);
    }

    @Test
    public void testNotLoggedIn() throws Exception {
        when(session.getId()).thenReturn("session1");

        GameWebSocketCreator creator = new GameWebSocketCreator(accountService, roomManager);

        assertNull(creator.createWebSocket(request, response));
    }
}