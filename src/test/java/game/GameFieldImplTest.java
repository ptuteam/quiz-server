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
import websocket.WebSocketService;

import java.util.HashSet;
import java.util.Set;

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
    public void testCheckPlayerCorrectAnswer() throws Exception {
        QuestionHelper questionHelper = mock(QuestionHelper.class);
        PowerMockito.whenNew(QuestionHelper.class).withNoArguments().thenReturn(questionHelper);
        when(questionHelper.checkAnswer(anyInt(), anyString())).thenReturn(true);

        gameField = new GameFieldImpl(webSocketService, session);

        Player player = mock(Player.class);
        UserProfile userProfile = mock(UserProfile.class);
        when(player.getUserProfile()).thenReturn(userProfile);

        PowerMockito.mockStatic(ConfigGeneral.class);
        when(ConfigGeneral.getPointsPerQuestion()).thenReturn(1);

        gameField.checkPlayerAnswer(player, "answer");
        verify(webSocketService, atLeastOnce()).notifyOnCorrectAnswer(player, true);
        verify(session, atLeastOnce()).increaseScore(player.getUserProfile(), ConfigGeneral.getPointsPerQuestion());
    }

    @Test
    public void testCheckPlayerIncorrectAnswer() throws Exception {
        QuestionHelper questionHelper = mock(QuestionHelper.class);
        PowerMockito.whenNew(QuestionHelper.class).withNoArguments().thenReturn(questionHelper);
        when(questionHelper.checkAnswer(anyInt(), anyString())).thenReturn(false);

        gameField = new GameFieldImpl(webSocketService, session);

        Player player = mock(Player.class);
        gameField.checkPlayerAnswer(player, "answer");
        verify(webSocketService, atLeastOnce()).notifyOnCorrectAnswer(player, false);
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