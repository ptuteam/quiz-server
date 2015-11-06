package websocket;

import com.google.gson.JsonObject;
import game.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

/**
 * Created by dima on 03.11.15.
 */
@RunWith(MockitoJUnitRunner.class)
public class WebSocketServiceImplTest {

    @Mock
    private Player player1;

    @Mock
    private GameWebSocket webSocket1;

    @Mock
    private Player player2;

    @Mock
    private GameWebSocket webSocket2;

    private Set<Player> players;

    private WebSocketService webSocketService;

    @Before
    public void setUp() {
        when(player1.getConnection()).thenReturn(webSocket1);
        when(player2.getConnection()).thenReturn(webSocket2);

        webSocketService = new WebSocketServiceImpl();
        players = new HashSet<>();
        players.add(player1);
        players.add(player2);
    }

    @Test
    public void testNotifyNewScores() throws Exception {
        when(player1.getUserEmail()).thenReturn("1");
        when(player1.getScore()).thenReturn(3);

        when(player2.getUserEmail()).thenReturn("2");
        when(player2.getScore()).thenReturn(4);

        webSocketService.notifyNewScores(players);
        verify(webSocket1, atMost(1)).onNewScores(anyMapOf(String.class, Integer.class));
        verify(webSocket2, atMost(1)).onNewScores(anyMapOf(String.class, Integer.class));
    }

    @Test
    public void testNotifyStartGame() throws Exception {
        webSocketService.notifyStartGame(players);
        verify(webSocket1, atMost(1)).onStartGame(players);
        verify(webSocket2, atMost(1)).onStartGame(players);
    }

    @Test
    public void testNotifyGameOver() throws Exception {
        webSocketService.notifyGameOver(players, player1);
        verify(webSocket1, atMost(1)).onGameOver(player1);
        verify(webSocket2, atMost(1)).onGameOver(player1);
    }

    @Test
    public void testNotifyNewQuestion() throws Exception {
        JsonObject questionObject = new JsonObject();
        webSocketService.notifyNewQuestion(players, questionObject);
        verify(webSocket1, atMost(1)).onNewQuestionAsk(questionObject);
        verify(webSocket2, atMost(1)).onNewQuestionAsk(questionObject);
    }

    @Test
    public void testNotifyPlayerDisconnect() throws Exception {
        webSocketService.notifyPlayerDisconnect(players, player1);
        verify(webSocket1, atMost(1)).onEnemyDisconnect(player1);
        verify(webSocket2, atMost(1)).onEnemyDisconnect(player1);

    }

    @Test
    public void testNotifyNewRound() throws Exception {
        webSocketService.notifyNewRound(players, 1);
        verify(webSocket1, atMost(1)).onNewRoundStart(1);
        verify(webSocket2, atMost(1)).onNewRoundStart(1);
    }

    @Test
    public void testNotifyNewPlayerConnect() throws Exception {
        webSocketService.notifyNewPlayerConnect(players, player1);
        verify(webSocket1, atMost(1)).onNewPlayerConnect(player1);
        verify(webSocket2, atMost(1)).onNewPlayerConnect(player1);
    }

    @Test
    public void testNotifyOnCorrectAnswer() throws Exception {
        webSocketService.notifyOnCorrectAnswer(player1, true);
        verify(webSocket1, atMost(1)).onCorrectAnswer(true);
    }

    @Test
    public void testNotifyAboutPlayersInRoom() throws Exception {
        webSocketService.notifyAboutPlayersInRoom(player1, players);
        verify(webSocket1, atMost(1)).listPlayersInRoom(players);
    }
}