package websocket;

import game.Player;
import game.Question;

import java.util.*;

/**
 * alex on 24.10.15.
 */
public class WebSocketServiceImpl implements WebSocketService {

    @Override
    public void notifyNewScores(Collection<Player> players) {
        for (Player player : players) {
            Set<Player> opponents = new HashSet<>(players);
            opponents.remove(player);
            player.getConnection().onNewScores(player, opponents);
        }
    }

    @Override
    public void notifyStartGame(Collection<Player> players) {
        for (Player player : players) {
            Set<Player> opponents = new HashSet<>(players);
            opponents.remove(player);
            player.getConnection().onStartGame(player, opponents);
        }
    }

    @Override
    public void notifyGameOver(Collection<Player> players, Player winner) {
        for (Player player : players) {
            player.getConnection().onGameOver(winner);
        }
    }

    @Override
    public void notifyNewQuestion(Collection<Player> players, Question question) {
        for (Player player : players) {
            player.getConnection().onNewQuestionAsk(question);
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
    public void notifyAboutPlayersInRoom(Player player, Collection<Player> players, long roomId) {
        player.getConnection().listPlayersInRoom(players, roomId);
    }

    @Override
    public void notifyPlayersAnswers(Collection<Player> players, String correctAnswer, Map<String, String> playersAnswers) {
        for (Player player : players) {
            player.getConnection().listPlayersAnswers(correctAnswer, playersAnswers);
        }
    }
}
