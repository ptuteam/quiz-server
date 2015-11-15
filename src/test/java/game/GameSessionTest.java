package game;

import model.UserProfile;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * alex on 06.11.15.
 */
@SuppressWarnings("unused")
@RunWith(MockitoJUnitRunner.class)
public class GameSessionTest {

    @Mock
    private Room room;

    @Mock
    private Player player1;

    @Mock
    private Player player2;

    private GameSession gameSession;

    @Before
    public void setUp() {
        when(player1.getUserProfile()).thenReturn(new UserProfile("1", "1", "1", "1"));
        when(player1.getUserEmail()).thenReturn("1");
        when(player2.getUserProfile()).thenReturn(new UserProfile("2", "2", "2", "2"));
        when(player2.getUserEmail()).thenReturn("2");
        Map<String, Player> map = new HashMap<>();
        map.put(player1.getUserEmail(), player1);
        map.put(player2.getUserEmail(), player2);
        gameSession = new GameSession(map, room);
    }

    @Test
    public void testGetPlayers() throws Exception {
        assertTrue(gameSession.getPlayers().contains(player1));
        assertTrue(gameSession.getPlayers().contains(player2));
        assertEquals(2, gameSession.getPlayers().size());
    }

    @Test
    public void testIncreaseScore() throws Exception {
        gameSession.increaseScore(player1.getUserProfile(), 2);
        verify(player1, atLeastOnce()).increaseScore(2);
    }

    @Test
    public void testGetWinner() throws Exception {
        when(player1.getScore()).thenReturn(7);
        when(player2.getScore()).thenReturn(10);
        assertEquals(player2, gameSession.getWinner());
        verify(player1, atLeastOnce()).getScore();
        verify(player2, atLeastOnce()).getScore();
    }

    @Test
    public void testGetWinnerWithIndentScores() throws Exception {
        when(player1.getScore()).thenReturn(10);
        when(player2.getScore()).thenReturn(10);
        assertNull(gameSession.getWinner());
        verify(player1, atLeastOnce()).getScore();
        verify(player2, atLeastOnce()).getScore();
    }

    @Test
    public void testStopGame() throws Exception {
        gameSession.stopGame();
        verify(player1, atLeastOnce()).updateUserGlobalScore();
        verify(player2, atLeastOnce()).updateUserGlobalScore();
        verify(room, atLeastOnce()).gameOver();
    }
}