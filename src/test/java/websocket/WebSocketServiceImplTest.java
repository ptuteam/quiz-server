package websocket;

import game.Player;
import game.Question;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.*;

/**
 * Created by dima on 03.11.15.
 */
@SuppressWarnings("unused")
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
        verify(webSocket1, atMost(1)).onNewScores(any(Player.class), anyCollectionOf(Player.class));
        verify(webSocket2, atMost(1)).onNewScores(any(Player.class), anyCollectionOf(Player.class));
    }

    @Test
    public void testNotifyStartGame() throws Exception {
        webSocketService.notifyStartGame(players);
        verify(webSocket1, atMost(1)).onStartGame(player1, players);
        verify(webSocket2, atMost(1)).onStartGame(player2, players);
    }

    @Test
    public void testNotifyGameOver() throws Exception {
        webSocketService.notifyGameOver(players, player1);
        verify(webSocket1, atMost(1)).onGameOver(player1);
        verify(webSocket2, atMost(1)).onGameOver(player1);
    }

    @Test
    public void testNotifyNewQuestion() throws Exception {
        Question question = mock(Question.class);
        webSocketService.notifyNewQuestion(players, question);
        verify(webSocket1, atMost(1)).onNewQuestionAsk(question);
        verify(webSocket2, atMost(1)).onNewQuestionAsk(question);
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
    public void testNotifyPlayersAnswers() throws Exception {
        @SuppressWarnings("unchecked") Map<String, String> playersAnswers = mock(Map.class);
        String correctAnswer = "correctAnswer";
        webSocketService.notifyPlayersAnswers(players, correctAnswer, playersAnswers);
        verify(webSocket1, atLeastOnce()).listPlayersAnswers(correctAnswer, playersAnswers);
        verify(webSocket2, atLeastOnce()).listPlayersAnswers(correctAnswer, playersAnswers);
    }

    @Test
    public void testNotifyAboutPlayersInRoom() throws Exception {
        webSocketService.notifyAboutPlayersInRoom(player1, players, 1);
        verify(webSocket1, atMost(1)).listPlayersInRoom(players, 1);
    }
}