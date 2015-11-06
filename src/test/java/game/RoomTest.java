package game;

import model.UserProfile;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import websocket.GameWebSocket;
import websocket.WebSocketServiceImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Created by dima on 04.11.15.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ Room.class })
public class RoomTest {

    @Mock
    private GameWebSocket webSocket;

    @Mock
    private WebSocketServiceImpl webSocketService;

    private Room room;
    private UserProfile user;

    @Before
    public void setUp() {
        room = new Room(webSocketService);
        user = new UserProfile("first", "last", "email", "avatar");
    }

    @Test
    public void testGetState() throws Exception {
        assertEquals(Room.States.WATING, room.getState());

        room.startGame();
        assertEquals(Room.States.PLAYING, room.getState());
    }

    @Test
    public void testGetPlayerByUser() throws Exception {
        assertEquals(null, room.getPlayerByUser(user));

        room.connectUser(user, webSocket);
        assertEquals(user, room.getPlayerByUser(user).getUserProfile());
    }

    @Test
    public void testConnectUser() throws Exception {
        room.connectUser(user, webSocket);
        assertEquals(room.getPlayersCount(), 1);
    }

    @Test
    public void testDisconnectUser() throws Exception {
        room.connectUser(user, webSocket);
        assertEquals(room.getPlayersCount(), 1);

        room.disconnectUser(user);
        assertEquals(room.getPlayersCount(), 0);
    }

    @Test
    public void testGameOver() throws Exception {
        room.startGame();
        assertEquals(Room.States.PLAYING, room.getState());
        room.gameOver();
        assertEquals(Room.States.WATING, room.getState());
        assertEquals(0, room.getPlayersCount());
    }

    @Test
    public void testStartGame() throws Exception {
        GameFieldImpl gameField = mock(GameFieldImpl.class);
        PowerMockito.whenNew(GameFieldImpl.class).withAnyArguments().thenReturn(gameField);

        room.startGame();
        assertEquals(room.getState(), Room.States.PLAYING);
        verify(gameField, atLeastOnce()).play();
    }

    @Test
    public void testGetPlayers() throws Exception {
        room.connectUser(user, webSocket);
        Player player = room.getPlayerByUser(user);
        assertTrue(room.getPlayers().contains(player));
        assertEquals(room.getPlayersCount(), 1);
    }

    @Test
    public void testCheckAnswer() throws Exception {
        GameFieldImpl gameField = mock(GameFieldImpl.class);
        PowerMockito.whenNew(GameFieldImpl.class).withAnyArguments().thenReturn(gameField);

        room.connectUser(user, webSocket);
        room.startGame();
        room.checkAnswer(user, "answer");
        verify(gameField, atLeastOnce()).checkPlayerAnswer(room.getPlayerByUser(user), "answer");
    }

    @Test
    public void testGetPlayersCount() {
        room.connectUser(user, webSocket);
        assertEquals(room.getPlayersCount(), 1);

        room.connectUser(new UserProfile("d", "d", "d", "d"), webSocket);
        assertEquals(room.getPlayersCount(), 2);
    }
}