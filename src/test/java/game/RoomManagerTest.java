package game;

import model.UserProfile;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import utils.ConfigGeneral;
import websocket.GameWebSocket;
import websocket.WebSocketServiceImpl;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

/**
 * alex on 06.11.15.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigGeneral.class})
public class RoomManagerTest {

    @Mock
    private UserProfile user;

    @Mock
    private GameWebSocket webSocket;

    @Mock
    private WebSocketServiceImpl webSocketService;

    private RoomManager roomManager;

    @Before
    public void setUp() {
        roomManager = new RoomManager(webSocketService);
        PowerMockito.mockStatic(ConfigGeneral.class);
        when(ConfigGeneral.getRoomsCount()).thenReturn(1);
        when(ConfigGeneral.getMaxPlayersPerRoom()).thenReturn(3);
    }

    @Test
    public void testConnectUser() throws Exception {
        Room room = roomManager.connectUser(user, webSocket);
        assertEquals(user, new ArrayList<>(room.getPlayers()).get(0).getUserProfile());
        assertEquals(1, room.getPlayersCount());
        assertEquals("{\"rooms\":[{\"id\":\"someId\",\"playersCount\":1}]}", roomManager.getRoomsInforamtion().toString());
    }

    @Test
    public void testConnectUserWithWaitingRooms() {
        roomManager.connectUser(user, webSocket);
        Room room = roomManager.connectUser(new UserProfile("q", "q", "q", "q"), webSocket);
        assertEquals(2, room.getPlayersCount());
        assertEquals("{\"rooms\":[{\"id\":\"someId\",\"playersCount\":2}]}", roomManager.getRoomsInforamtion().toString());
    }

    @Test
    public void testConnectUserWithNoEmtyRooms() {
        when(ConfigGeneral.getRoomsCount()).thenReturn(0);

        Room room = roomManager.connectUser(user, webSocket);
        assertNull(room);
    }
}