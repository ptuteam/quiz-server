package websocket;

import com.google.gson.JsonObject;
import game.Player;

import java.util.Collection;

/**
 * alex on 22.10.15.
 */
public interface WebSocketService {
    void notifyNewScore(Collection<Player> players, Player playerWithNewScore);

    void notifyStartGame(Collection<Player> players);

    void notifyGameOver(Collection<Player> players, Player winner);

    void notifyNewQuestion(Collection<Player> players, JsonObject questionObject);

    void notifyPlayerDisconnect(Collection<Player> players, Player disconnectedPlayer);
}