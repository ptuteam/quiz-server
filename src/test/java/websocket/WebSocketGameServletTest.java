package websocket;

import game.RoomManager;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import utils.AccountService;

import static org.mockito.Mockito.*;

/**
 * alex on 06.11.15.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({WebSocketGameServlet.class})
public class WebSocketGameServletTest {

    @Mock
    private AccountService accountService;

    @Mock
    private RoomManager roomManager;

    private WebSocketGameServlet servlet;

    @Before
    public void setUp() {
         servlet = new WebSocketGameServlet(accountService, roomManager);
    }

    @Test
    public void testConfigure() throws Exception {
        GameWebSocketCreator creator = mock(GameWebSocketCreator.class);
        PowerMockito.whenNew(GameWebSocketCreator.class).withArguments(accountService, roomManager).thenReturn(creator);
        WebSocketServletFactory factory = mock(WebSocketServletFactory.class);
        WebSocketPolicy policy = mock(WebSocketPolicy.class);
        when(factory.getPolicy()).thenReturn(policy);

        servlet.configure(factory);

        verify(policy, atLeastOnce()).setIdleTimeout(anyLong());
        verify(factory, atLeastOnce()).setCreator(creator);
    }
}