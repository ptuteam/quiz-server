package websocket;

import game.Player;
import game.Question;

import java.util.Collection;

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

    void notifyOnCorrectAnswer(Player player, boolean correct);

    void notifyAboutPlayersInRoom(Player player, Collection<Player> players);
}