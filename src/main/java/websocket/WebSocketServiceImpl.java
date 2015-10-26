package websocket;

import com.google.gson.JsonObject;
import game.Player;

import java.util.Collection;

/**
 * alex on 24.10.15.
 */
public class WebSocketServiceImpl implements WebSocketService {

    @Override
    public void notifyNewScore(Collection<Player> players, Player playerWithNewScore) {
        for (Player player : players) {
            player.getConnection().onChangeScore(playerWithNewScore);
        }
    }

    @Override
    public void notifyStartGame(Collection<Player> players) {
        for (Player player : players) {
            player.getConnection().onStartGame(players);
        }
    }

    @Override
    public void notifyGameOver(Collection<Player> players, Player winner) {
        for (Player player : players) {
            player.getConnection().onGameOver(winner);
        }
    }

    @Override
    public void notifyNewQuestion(Collection<Player> players, JsonObject questionObject) {
        for (Player player : players) {
            player.getConnection().onNewQuestionAsk(questionObject);
        }
    }

    @Override
    public void notifyPlayerDisconnect(Collection<Player> players, Player disconnectedPlayer) {
        for (Player player : players) {
            player.getConnection().onEnemyDisconnect(disconnectedPlayer);
        }
    }
}
