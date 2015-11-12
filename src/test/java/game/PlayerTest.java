package game;

import model.UserProfile;
import org.junit.Before;
import org.junit.Test;
import websocket.GameWebSocket;
import websocket.WebSocketServiceImpl;

import static org.junit.Assert.*;

/**
 * Created by dima on 02.11.15.
 */
@SuppressWarnings("unused")
public class PlayerTest {

    private Player player;

    @Before
    public void setUp() {
        player = new Player(new UserProfile("first", "last", "email", "avatar"));
    }

    @Test
    public void testGetUserProfile() throws Exception {
        assertEquals("first", player.getUserProfile().getFirstName());
    }

    @Test
    public void testGetUserEmail() throws Exception {
        assertEquals("email", player.getUserEmail());
    }

    @Test
    public void testConnection() throws Exception {
        RoomManager roomManager = new RoomManager(new WebSocketServiceImpl());
        GameWebSocket gameWebSocket = new GameWebSocket(player.getUserProfile(), roomManager);

        player.setConnection(gameWebSocket);
        assertEquals(gameWebSocket, player.getConnection());
    }

    @Test
    public void testGetScore() throws Exception {
        assertEquals(0, player.getScore());
    }

    @Test
    public void testIncreaseScore() throws Exception {
        player.increaseScore(5);
        assertEquals(5, player.getScore());
    }

    @Test
    public void testUpdateUserGlobalScore() throws Exception {
        UserProfile userProfile = player.getUserProfile();
        userProfile.setScore(5);
        player.increaseScore(5);
        player.updateUserGlobalScore();

        assertEquals(5, player.getScore());
        assertEquals(10, userProfile.getScore());
    }
}