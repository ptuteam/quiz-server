package websocket;

import database.connection.DatabaseConnection;
import database.executor.TExecutor;
import game.Player;
import game.Question;
import game.Room;
import game.RoomManager;
import model.UserProfile;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import utils.ConfigGeneral;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import static org.mockito.Mockito.*;

/**
 * alex on 06.11.15.
 */
@SuppressWarnings("unused")
@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigGeneral.class})
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

        gameWebSocket = new GameWebSocket(user, roomManager, 0, 0);
        when(roomManager.connectUser(user, gameWebSocket, 0, false)).thenReturn(room);
        gameWebSocket.onOpen(session);

        PowerMockito.mockStatic(ConfigGeneral.class);
        when(ConfigGeneral.getDbType()).thenReturn("jdbc:mysql://");
        when(ConfigGeneral.getDbHostName()).thenReturn("localhost:");
        when(ConfigGeneral.getDbPort()).thenReturn("3306/");
        when(ConfigGeneral.getDbNameQuiz()).thenReturn("test_quiz_db?");
        when(ConfigGeneral.getDbNameUsers()).thenReturn("test_quiz_users_db?");
        when(ConfigGeneral.getDbLogin()).thenReturn("user=test_quiz_user&");
        when(ConfigGeneral.getDbPassword()).thenReturn("password=secret");
        ConfigGeneral.loadConfig();
    }

    @After
    public void tearDown() throws SQLException {
        String query = "TRUNCATE TABLE users;";

        TExecutor.execQuery(DatabaseConnection.getUsersConnection(), query);
    }

    @Test
    public void testOnOpen() throws Exception {
        verify(roomManager, atLeastOnce()).connectUser(user, gameWebSocket, 0, false);
    }

    @Test
    public void testOnOpenWithNoEmptyRooms() throws IOException {
        gameWebSocket = new GameWebSocket(user, roomManager, 0, 0);
        when(roomManager.connectUser(user, gameWebSocket, 0, false)).thenReturn(null);
        gameWebSocket.onOpen(session);
        verify(roomManager, atLeastOnce()).connectUser(user, gameWebSocket, 0, false);
        verify(remoteEndpoint, atLeastOnce()).sendString("{\"code\":7,\"description\":\"no empty rooms\"}");
    }

    @Test
    public void testOnAnswerMessage() throws Exception {
        when(room.getState()).thenReturn(Room.States.PLAYING);
        gameWebSocket.onMessage("{\"code\":6,\"answer\":\"ans\"}");
        verify(room, atLeastOnce()).setAnswer(user, "ans");
    }

    @Test
    public void testOnMessage() throws Exception {
        when(room.getState()).thenReturn(Room.States.PLAYING);
        gameWebSocket.onMessage("{\"code\":7,\"description\":\"no empty rooms\"}");
        verify(room, atMost(0)).setAnswer(any(UserProfile.class), anyString());

        when(room.getState()).thenReturn(Room.States.WATING);
        gameWebSocket.onMessage("{\"code\":6,\"answer\":\"no empty rooms\"}");
        verify(room, atMost(0)).setAnswer(any(UserProfile.class), anyString());
    }

    @Test
    public void testOnClose() throws Exception {
        gameWebSocket.onClose(0, "reason");
        verify(room, atLeastOnce()).disconnectUser(user);
    }

    @Test
    public void testOnStartGame() throws IOException {
        List<Player> opponents = new ArrayList<>();
        Player player1 = new Player(new UserProfile("first1", "last1", "email1", "avatar1"));
        player1.increaseScore(5);
        Player player2 = new Player(new UserProfile("first2", "last2", "email2", "avatar2"));
        player2.increaseScore(7);
        Player player3 = new Player(new UserProfile("first3", "last3", "email3", "avatar3"));
        player3.increaseScore(10);
        opponents.add(player2);
        opponents.add(player3);
        gameWebSocket.onStartGame(player1, opponents);
        verify(remoteEndpoint, atLeastOnce()).sendString("{\"code\":1,\"opponents\":[{\"first_name\":" +
                "\"first2\",\"last_name\":\"last2\",\"email\":\"email2\",\"avatar\":\"avatar2\",\"score\":7}," +
                "{\"first_name\":\"first3\",\"last_name\":\"last3\",\"email\":\"email3\",\"avatar\":\"avatar3\"," +
                "\"score\":10}],\"description\":\"start\",\"player\":{\"first_name\":\"first1\",\"last_name\":" +
                "\"last1\",\"email\":\"email1\",\"avatar\":\"avatar1\",\"score\":5}}");
    }

    @Test
    public void testOnNewScores() throws IOException {
        Player player = new Player(new UserProfile("first1", "last1", "email1", "avatar1"));
        List<Player> opponents = new ArrayList<>();
        opponents.add(new Player(new UserProfile("first2", "last2", "email2", "avatar2")));
        opponents.get(0).increaseScore(5);
        gameWebSocket.onNewScores(player, opponents);
        verify(remoteEndpoint, atLeastOnce()).sendString("{\"code\":2,\"description\":\"new players scores\"," +
                "\"player\":{\"email\":\"email1\",\"score\":0},\"opponents\":[{\"email\":\"email2\",\"score\":5}]}");
    }

    @Test
    public void testOnGameOver() throws IOException {
        Player winner = mock(Player.class);
        when(winner.getUserEmail()).thenReturn("email");
        gameWebSocket.onGameOver(winner);
        verify(remoteEndpoint, atLeastOnce()).sendString("{\"code\":3,\"winner\":\"email\"," +
                "\"description\":\"finish\"}");
    }

    @Test
    public void testOnEnemyDisconnect() throws IOException {
        Player disconnectedPlayer = mock(Player.class);
        when(disconnectedPlayer.getUserEmail()).thenReturn("email");
        gameWebSocket.onEnemyDisconnect(disconnectedPlayer);
        verify(remoteEndpoint, atLeastOnce()).sendString("{\"code\":4,\"description\":\"player disconnect\"," +
                "\"player\":\"email\"}");
    }

    @Test
    public void testOnNewQuestionAskType1() throws IOException {
        Question question = new Question("que", 1, new String[]{"1", "2", "3", "4"}, "1");
        gameWebSocket.onNewQuestionAsk(question);
        verify(remoteEndpoint, atLeastOnce()).sendString("{\"code\":5,\"question\":" +
                "{\"type\":1,\"title\":\"que\",\"answers\":[\"1\",\"2\",\"3\",\"4\"]},\"description\":\"new question\"}");
    }

    @Test
    public void testOnNewQuestionAskType2() throws IOException {
        Question question = new Question("que", 2, null, "1");
        gameWebSocket.onNewQuestionAsk(question);
        verify(remoteEndpoint, atLeastOnce()).sendString("{\"code\":5,\"question\":" +
                "{\"type\":2,\"title\":\"que\"},\"description\":\"new question\"}");
    }

    @Test
    public void testOnNoEmptyRooms() throws IOException {
        gameWebSocket.onNoEmptyRooms();
        verify(remoteEndpoint, atLeastOnce()).sendString("{\"code\":7,\"description\":\"no empty rooms\"}");
    }

    @Test
    public void testOnNewPlayerConnect() throws IOException {
        Player newPlayer = mock(Player.class);
        when(newPlayer.getUserProfile()).thenReturn(new UserProfile("first", "last", "email", "avatar"));
        gameWebSocket.onNewPlayerConnect(newPlayer);
        verify(remoteEndpoint, atLeastOnce()).sendString("{\"code\":8,\"description\":" +
                "\"new player connect\",\"player\":{\"first_name\":\"first\",\"last_name\":\"last\"," +
                "\"email\":\"email\",\"avatar\":\"avatar\",\"score\":0}}");
    }

    @Test
    public void testOnNewRoundStart() throws IOException {
        gameWebSocket.onNewRoundStart(0);
        verify(remoteEndpoint, atLeastOnce()).sendString("{\"code\":9,\"round\":0," +
                "\"description\":\"new round start\"}");
    }

    @Test
    public void testListPlayersAnswers() throws IOException {
        Map<String, String> playersAnswers = new HashMap<>();
        for (int i = 0; i < 3; ++i) {
            playersAnswers.put("email" + i, "answer" + i);
        }
        gameWebSocket.listPlayersAnswers("corr", playersAnswers);
        verify(remoteEndpoint, atLeastOnce()).sendString("{\"code\":10,\"correct\":\"corr\"," +
                "\"answers\":{\"email2\":\"answer2\",\"email1\":\"answer1\",\"email0\":\"answer0\"}," +
                "\"description\":\"players answers\"}");
    }

    @Test
    public void testListPlayersInRoom() throws IOException {
        Set<Player> players = new HashSet<>();
        Player player = mock(Player.class);
        when(player.getUserProfile()).thenReturn(new UserProfile("first", "last", "email", "avatar"));
        player.getUserProfile().setScore(5);
        players.add(player);
        gameWebSocket.listPlayersInRoom(players, 1);
        verify(remoteEndpoint, atLeastOnce()).sendString("{\"code\":11,\"players\":" +
                "[{\"first_name\":\"first\",\"last_name\":\"last\",\"email\":\"email\"," +
                "\"avatar\":\"avatar\",\"score\":5}],\"description\":\"players in room\",\"roomId\":1}");
    }
}