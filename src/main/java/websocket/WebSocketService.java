package websocket;

import game.Player;
import game.Question;

import java.util.Collection;
import java.util.Map;

/**
 * alex on 22.10.15.
 */
public interface WebSocketService {
    void notifyNewScores(Collection<Player> players);

    void notifyStartGame(Collection<Player> players);

    void notifyGameOver(Collection<Player> players, Player winner);

    void notifyNewQuestion(Collection<Player> players, Question question);

    void notifyPlayerDisconnect(Collection<Player> players, Player disconnectedPlayer);

    void notifyNewRound(Collection<Player> players, int round);

    void notifyNewPlayerConnect(Collection<Player> players, Player newPlayer);

    void notifyAboutPlayersInRoom(Player player, Collection<Player> players, long roomId);

    void notifyPlayersAnswers(Collection<Player> players, String correctAnswer, Map<String, String> playersAnswers);
}