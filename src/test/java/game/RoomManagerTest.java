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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

/**
 * alex on 06.11.15.
 */
@SuppressWarnings("unused")
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
        when(user.getEmail()).thenReturn("email");
        roomManager = new RoomManager(webSocketService);
        PowerMockito.mockStatic(ConfigGeneral.class);
        when(ConfigGeneral.getRoomsCount()).thenReturn(2);
        when(ConfigGeneral.getMaxPlayersPerRoom()).thenReturn(3);
    }

    @Test
    public void testConnectUserToNewRoom() throws Exception {
        Room room = roomManager.connectUser(user, webSocket, 0, false);
        assertEquals(user, new ArrayList<>(room.getPlayers()).get(0).getUserProfile());
        assertEquals("{\"rooms\":[{\"id\":" + room.getRoomId() + ",\"playersCount\":1}]}", roomManager.getRoomsInforamtion().toString());
    }

    @Test
    public void testConnectUserToWaitingRoom() {
        roomManager.connectUser(user, webSocket, 0, false);
        Room room = roomManager.connectUser(new UserProfile("q", "q", "q", "q"), webSocket, 0, false);
        assertEquals("{\"rooms\":[{\"id\":" + room.getRoomId() + ",\"playersCount\":2}]}", roomManager.getRoomsInforamtion().toString());
    }

    @Test
    public void testConnectUserToNewPrivateRoom() {
        Room privateRoom = roomManager.connectUser(user, webSocket, 0, true);
        Room publicRoom = roomManager.connectUser(new UserProfile("q", "q", "q", "q"), webSocket, 0, false);
        assertNotEquals(privateRoom, publicRoom);
        assertEquals("{\"rooms\":[{\"id\":" + privateRoom.getRoomId() + ",\"playersCount\":1}," +
                "{\"id\":" + publicRoom.getRoomId() + ",\"playersCount\":1}]}", roomManager.getRoomsInforamtion().toString());
    }

    @Test
    public void testConnUserToWaitPrivRoomById() {
        Room privateRoom1 = roomManager.connectUser(user, webSocket, 0, true);
        Room privateRoom2 = roomManager.connectUser(new UserProfile("q", "q", "q", "q"), webSocket, privateRoom1.getRoomId(), true);
        assertEquals(privateRoom1, privateRoom2);
        assertEquals("{\"rooms\":[{\"id\":" + privateRoom1.getRoomId() + ",\"playersCount\":2}]}", roomManager.getRoomsInforamtion().toString());
    }

    @Test
    public void testNotConnectUserToWaitPrivRoom() {
        Room privateRoom1 = roomManager.connectUser(user, webSocket, 0, true);
        Room privateRoom2 = roomManager.connectUser(new UserProfile("q", "q", "q", "q"), webSocket, 0, true);
        assertNotEquals(privateRoom1, privateRoom2);
        assertEquals("{\"rooms\":[{\"id\":" + privateRoom1.getRoomId() + ",\"playersCount\":1}," +
                "{\"id\":" + privateRoom2.getRoomId() + ",\"playersCount\":1}]}", roomManager.getRoomsInforamtion().toString());
    }

    @Test
    public void testConnectUserToRoomById() {
        Room room1 = roomManager.connectUser(user, webSocket, 0, false);
        Room room2 = roomManager.connectUser(new UserProfile("q", "q", "q", "q"), webSocket, room1.getRoomId(), false);
        assertEquals(room1, room2);
        assertEquals("{\"rooms\":[{\"id\":" + room1.getRoomId() + ",\"playersCount\":2}]}", roomManager.getRoomsInforamtion().toString());
    }

    @Test
    public void testConnectUserWithNoEmtyRooms() {
        when(ConfigGeneral.getRoomsCount()).thenReturn(0);

        Room room = roomManager.connectUser(user, webSocket, 0, false);
        assertNull(room);
    }
}