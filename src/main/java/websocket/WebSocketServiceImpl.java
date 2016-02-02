package websocket;

import game.MapSegment;
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
    public void notifyStartBlitzGame(Collection<Player> players) {
        for (Player player : players) {
            Set<Player> opponents = new HashSet<>(players);
            opponents.remove(player);
            player.getConnection().onStartBlitzGame(player, opponents);
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

    @Override
    public void notifyPlayerTurnStart(Collection<Player> players, Player player, Collection<Integer> allowableMoveForPlayer) {
        for (Player p : players) {
            p.getConnection().onPlayerTurnStart(player, allowableMoveForPlayer);
        }
    }

    @Override
    public void notifyPlayerTurnFinish(Collection<Player> players, Player player, Integer invadedSegmentId) {
        for (Player p : players) {
            p.getConnection().onPlayerTurnFinish(player, invadedSegmentId);
        }
    }

    @Override
    public void notifyRoundFinish(Collection<Player> players) {
        for (Player player : players) {
            player.getConnection().onRoundFinish();
        }
    }

    @Override
    public void notifyStartMapGame(Collection<Player> players, Collection<MapSegment> mapSegments) {
        for (Player player : players) {
            Set<Player> opponents = new HashSet<>(players);
            opponents.remove(player);
            player.getConnection().onStartMapGame(player, opponents, mapSegments);
        }
    }

    @Override
    public void notifyPlayerSelectSegment(Collection<Player> players, int segmentId, String owner) {
        for (Player p : players) {
            p.getConnection().onPlayerSelectedSegment(segmentId, owner);
        }
    }

    @Override
    public void notifyPlayerIncorrectSelectSegment(Player player) {
        player.getConnection().onIncorrectSelectSegment();
    }
}
