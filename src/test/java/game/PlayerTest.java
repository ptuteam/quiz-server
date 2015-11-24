package game;

import database.connection.DatabaseConnection;
import database.executor.TExecutor;
import model.UserProfile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import utils.ConfigGeneral;
import websocket.GameWebSocket;
import websocket.WebSocketServiceImpl;

import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by dima on 02.11.15.
 */
@SuppressWarnings("unused")
@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigGeneral.class})
public class PlayerTest {

    private Player player;

    @Before
    public void setUp() {
        PowerMockito.mockStatic(ConfigGeneral.class);
        when(ConfigGeneral.getDbType()).thenReturn("jdbc:mysql://");
        when(ConfigGeneral.getDbHostName()).thenReturn("localhost:");
        when(ConfigGeneral.getDbPort()).thenReturn("3306/");
        when(ConfigGeneral.getDbNameQuiz()).thenReturn("test_quiz_db?");
        when(ConfigGeneral.getDbNameUsers()).thenReturn("test_quiz_users_db?");
        when(ConfigGeneral.getDbLogin()).thenReturn("user=test_quiz_user&");
        when(ConfigGeneral.getDbPassword()).thenReturn("password=secret");
        ConfigGeneral.loadConfig();

        player = new Player(new UserProfile("first", "last", "email", "avatar"));
    }

    @After
    public void tearDown() throws SQLException {
        String query = "TRUNCATE TABLE users;";

        TExecutor exec = new TExecutor();
        exec.execQuery(DatabaseConnection.getUsersConnection(), query);
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
        GameWebSocket gameWebSocket = new GameWebSocket(player.getUserProfile(), roomManager, 0, 0);

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