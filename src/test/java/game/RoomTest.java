package game;

import model.UserProfile;
import org.junit.Before;
import org.junit.Test;
import websocket.GameWebSocket;
import websocket.WebSocketServiceImpl;

import static org.junit.Assert.*;

/**
 * Created by dima on 04.11.15.
 */
public class RoomTest {
    private Room room;

    @Before
    public void setUp() throws Exception {
        room = new Room(new WebSocketServiceImpl());
    }

    @Test
    public void testGetState() throws Exception {
        assertEquals(Room.States.WATING, room.getState());

        room.startGame();
        assertEquals(Room.States.PLAYING, room.getState());
    }

    @Test
    public void testGetPlayerByUser() throws Exception {
        UserProfile user = new UserProfile("first", "last", "email", "avatar");
        assertEquals(null, room.getPlayerByUser(user));

        room.connectUser(user, new GameWebSocket(user, new RoomManager(new WebSocketServiceImpl())));
        assertEquals("email", room.getPlayerByUser(user).getUserEmail());
    }

    @Test
    public void testConnectUser() throws Exception {

    }

    @Test
    public void testDisconnectUser() throws Exception {

    }

    @Test
    public void testGameOver() throws Exception {
        room.gameOver();
        assertEquals(Room.States.WATING, room.getState());
        assertEquals(0, room.getPlayers().size());
    }

    @Test
    public void testStartGame() throws Exception {

    }

    @Test
    public void testGetPlayers() throws Exception {

    }

    @Test
    public void testCheckAnswer() throws Exception {

    }
}