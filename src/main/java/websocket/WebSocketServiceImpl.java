package websocket;

import com.google.gson.JsonObject;
import game.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * alex on 24.10.15.
 */
public class WebSocketServiceImpl implements WebSocketService {

    @Override
    public void notifyNewScores(Collection<Player> players) {
        Map<String, Integer> scoreMap = new HashMap<>();
        for (Player player : players) {
            scoreMap.put(player.getUserEmail(), player.getScore());
        }

        for (Player player : players) {
            player.getConnection().onNewScores(scoreMap);
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

    @Override
    public void notifyNewRound(Collection<Player> players, int round) {
        for (Player player : players) {
            player.getConnection().onNewRoundStart(round);
        }
    }

    @Override
    public void notifyNewPlayerConnect(Collection<Player> players, Player newPlayer) {
        for (Player player : players) {
            player.getConnection().onNewPlayerConnect(newPlayer);
        }
    }

    @Override
    public void notifyOnCorrectAnswer(Player player, boolean correct) {
        player.getConnection().onCorrectAnswer(correct);
    }

    @Override
    public void notifyAboutPlayersInRoom(Player player, Collection<Player> players) {
        player.getConnection().listPlayersInRoom(players);
    }
}
