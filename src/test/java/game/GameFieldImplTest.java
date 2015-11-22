package game;

import model.UserProfile;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import utils.ConfigGeneral;
import websocket.WebSocketService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * alex on 06.11.15.
 */
@SuppressWarnings("unused")
@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigGeneral.class, GameFieldImpl.class})
public class GameFieldImplTest {

    @Mock
    private WebSocketService webSocketService;

    @Mock
    private GameSession session;

    private GameField gameField;

    @Before
    public void setUp() {
        gameField = new GameFieldImpl(webSocketService, session);
    }

    @Test
    public void testSetPlayerAnswer() throws Exception {
        Room room = mock(Room.class);
        Player player = mock(Player.class);
        when(player.getUserEmail()).thenReturn("mail");
        Map<String, Player> playerMap = new HashMap<>();
        playerMap.put(player.getUserEmail(), player);
        GameSession session1 = new GameSession(playerMap, room);

        gameField = new GameFieldImpl(webSocketService, session1);
        gameField.setPlayerAnswer(player, "answer");
        assertTrue(session1.getPlayersAnswers(0).get(player.getUserEmail()).equals("answer"));
    }

    @Test
    public void testPlayerAnswerCorrect() throws Exception {
        Room room = mock(Room.class);
        Player player1 = mock(Player.class);
        when(player1.getUserProfile()).thenReturn(new UserProfile("1", "1", "1", "1"));
        when(player1.getUserEmail()).thenReturn("1");
        when(player1.getScore()).thenReturn(0);
        Player player2 = mock(Player.class);
        when(player1.getUserProfile()).thenReturn(new UserProfile("2", "2", "2", "2"));
        when(player2.getUserEmail()).thenReturn("2");
        when(player2.getScore()).thenReturn(0);
        Map<String, Player> playerMap = new HashMap<>();
        playerMap.put(player1.getUserEmail(), player1);
        playerMap.put(player2.getUserEmail(), player2);

        session = new GameSession(playerMap, room);
        gameField = new GameFieldImpl(webSocketService, session);
        gameField.setPlayerAnswer(player1, "answer1");
        gameField.setPlayerAnswer(player2, "answer2");
        assertTrue(session.getPlayersAnswers(0).get(player1.getUserEmail()).equals("answer1") && session.getPlayersAnswers(0).get(player2.getUserEmail()).equals("answer2"));
    }

    @Test
    public void testGameOver() throws Exception {
        Set<Player> players = new HashSet<>();
        Player winner = mock(Player.class);
        when(session.getPlayers()).thenReturn(players);
        when(session.getWinner()).thenReturn(winner);

        gameField.gameOver();
        verify(webSocketService, atLeastOnce()).notifyGameOver(players, winner);
        verify(session, atLeastOnce()).stopGame();
    }
}