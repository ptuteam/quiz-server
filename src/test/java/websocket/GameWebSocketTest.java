package websocket;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import game.Player;
import game.Room;
import game.RoomManager;
import model.UserProfile;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.*;

/**
 * alex on 06.11.15.
 */
@RunWith(MockitoJUnitRunner.class)
public class GameWebSocketTest {

    @Mock
    private UserProfile user;

    @Mock
    private RoomManager roomManager;

    @Mock
    private Room room;

    @Mock
    private Session session;

    @Mock
    private RemoteEndpoint remoteEndpoint;

    private GameWebSocket gameWebSocket;

    @Before
    public void setUp() {
        when(session.getRemote()).thenReturn(remoteEndpoint);

        gameWebSocket = new GameWebSocket(user, roomManager);
        when(roomManager.connectUser(user, gameWebSocket)).thenReturn(room);
        gameWebSocket.onOpen(session);
    }

    @Test
    public void testOnOpen() throws Exception {
        verify(roomManager, atLeastOnce()).connectUser(user, gameWebSocket);
    }

    @Test
    public void testOnOpenWithNoEmptyRooms() throws IOException {
        gameWebSocket = new GameWebSocket(user, roomManager);
        when(roomManager.connectUser(user, gameWebSocket)).thenReturn(null);
        gameWebSocket.onOpen(session);
        verify(roomManager, atLeastOnce()).connectUser(user, gameWebSocket);
        verify(remoteEndpoint, atLeastOnce()).sendString("{\"code\":\"7\",\"description\":\"no empty rooms\"}");
    }

    @Test
    public void testOnAnswerMessage() throws Exception {
        when(room.getState()).thenReturn(Room.States.PLAYING);
        gameWebSocket.onMessage("{\"code\":\"6\",\"answer\":\"ans\"}");
        verify(room, atLeastOnce()).checkAnswer(user, "ans");
    }

    @Test
    public void testOnMessage() throws Exception {
        when(room.getState()).thenReturn(Room.States.PLAYING);
        gameWebSocket.onMessage("{\"code\":\"7\",\"description\":\"no empty rooms\"}");
        verify(room, atMost(0)).checkAnswer(any(UserProfile.class), anyString());

        when(room.getState()).thenReturn(Room.States.WATING);
        gameWebSocket.onMessage("{\"code\":\"6\",\"answer\":\"no empty rooms\"}");
        verify(room, atMost(0)).checkAnswer(any(UserProfile.class), anyString());
    }

    @Test
    public void testOnClose() throws Exception {
        gameWebSocket.onClose(0, "reason");
        verify(room, atLeastOnce()).disconnectUser(user);
    }

    @Test
    public void testOnStartGame() throws IOException {
        Set<Player> players = new HashSet<>();
        Player player = mock(Player.class);
        when(player.getUserProfile()).thenReturn(new UserProfile("first", "last", "email", "avatar"));
        players.add(player);
        gameWebSocket.onStartGame(players);
        verify(remoteEndpoint, atLeastOnce()).sendString("{\"code\":\"1\",\"description\":\"start\"," +
                "\"players\":[{\"email\":\"email\",\"first_name\":\"first\",\"last_name\":\"last\"," +
                "\"avatar\":\"avatar\",\"score\":0}]}");
    }

    @Test
    public void testOnNewScores() throws IOException {
        Map<String, Integer> scoreMap = new HashMap<>();
        scoreMap.put("email1", 0);
        scoreMap.put("email2", 2);
        gameWebSocket.onNewScores(scoreMap);
        verify(remoteEndpoint, atLeastOnce()).sendString("{\"code\":\"2\",\"description\":\"new players scores\"," +
                "\"players\":[{\"email\":\"email2\",\"score\":2},{\"email\":\"email1\",\"score\":0}]}");
    }

    @Test
    public void testOnGameOver() throws IOException {
        Player winner = mock(Player.class);
        when(winner.getUserEmail()).thenReturn("email");
        gameWebSocket.onGameOver(winner);
        verify(remoteEndpoint, atLeastOnce()).sendString("{\"code\":\"3\",\"description\":\"finish\"," +
                "\"winner\":\"email\"}");
    }

    @Test
    public void testOnEnemyDisconnect() throws IOException {
        Player disconnectedPlayer = mock(Player.class);
        when(disconnectedPlayer.getUserEmail()).thenReturn("email");
        gameWebSocket.onEnemyDisconnect(disconnectedPlayer);
        verify(remoteEndpoint, atLeastOnce()).sendString("{\"code\":\"4\",\"description\":\"player disconnect\"," +
                "\"player\":\"email\"}");
    }

    @Test
    public void testOnNewQuestionAsk() throws IOException {
        JsonObject questionObject = new JsonObject();
        questionObject.addProperty("question", "que");
        JsonArray answersArray = new JsonArray();
        questionObject.add("answers", answersArray);
        answersArray.add("1");
        answersArray.add("2");
        answersArray.add("3");
        answersArray.add("4");
        gameWebSocket.onNewQuestionAsk(questionObject);
        verify(remoteEndpoint, atLeastOnce()).sendString("{\"question\":\"que\"," +
                "\"answers\":[\"1\",\"2\",\"3\",\"4\"],\"code\":\"5\",\"description\":\"new question\"}");
    }

    @Test
    public void testOnNoEmptyRooms() throws IOException {
        gameWebSocket.onNoEmptyRooms();
        verify(remoteEndpoint, atLeastOnce()).sendString("{\"code\":\"7\",\"description\":\"no empty rooms\"}");
    }

    @Test
    public void testOnNewPlayerConnect() throws IOException {
        Player newPlayer = mock(Player.class);
        when(newPlayer.getUserProfile()).thenReturn(new UserProfile("first", "last", "email", "avatar"));
        gameWebSocket.onNewPlayerConnect(newPlayer);
        verify(remoteEndpoint, atLeastOnce()).sendString("{\"code\":\"8\"," +
                "\"description\":\"new player connect\",\"player\":{\"email\":\"email\"," +
                "\"first_name\":\"first\",\"last_name\":\"last\",\"avatar\":\"avatar\",\"score\":0}}");
    }

    @Test
    public void testOnNewRoundStart() throws IOException {
        gameWebSocket.onNewRoundStart(0);
        verify(remoteEndpoint, atLeastOnce()).sendString("{\"code\":\"9\"," +
                "\"description\":\"new round start\",\"round\":0}");
    }

    @Test
    public void testOnCorrectAnswer() throws IOException {
        gameWebSocket.onCorrectAnswer(true);
        verify(remoteEndpoint, atLeastOnce()).sendString("{\"code\":\"10\"," +
                "\"description\":\"is answer correct?\",\"correct\":true}");
    }

    @Test
    public void testOnIncorrectAnswer() throws IOException {
        gameWebSocket.onCorrectAnswer(false);
        verify(remoteEndpoint, atLeastOnce()).sendString("{\"code\":\"10\"," +
                "\"description\":\"is answer correct?\",\"correct\":false}");
    }

    @Test
    public void testListPlayersInRoom() throws IOException {
        Set<Player> players = new HashSet<>();
        Player player = mock(Player.class);
        when(player.getUserProfile()).thenReturn(new UserProfile("first", "last", "email", "avatar"));
        players.add(player);
        gameWebSocket.listPlayersInRoom(players);
        verify(remoteEndpoint, atLeastOnce()).sendString("{\"code\":\"11\",\"description\":\"players in room\"," +
                "\"players\":[{\"email\":\"email\",\"first_name\":\"first\",\"last_name\":\"last\"," +
                "\"avatar\":\"avatar\",\"score\":0}]}");
    }
}