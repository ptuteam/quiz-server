package game;

import model.UserProfile;

import java.util.*;

/**
 * alex on 22.10.15.
 */
public class GameSession {
    private final long startTime;
    private final Room room;
    private final Map<String, Player> players;
    private final Map<Integer, Map<String, String>> playerAnswers = new HashMap<>();
    private final Set<Integer> askedQuestions = new HashSet<>();

    public GameSession(Map<String, Player> players, Room room) {
        startTime = new Date().getTime();
        this.players = players;
        this.room = room;
    }

    public Set<Player> getPlayers() {
        return new HashSet<>(players.values());
    }

    public void increaseScore(UserProfile user, int value) {
        players.get(user.getEmail()).increaseScore(value);
    }

    public long getSessionTime() {
        return new Date().getTime() - startTime;
    }

    public Player getWinner() {
        Player winner = null;

        for (Player player : getPlayers()) {
            if (winner == null || Integer.valueOf(player.getScore()).compareTo(winner.getScore()) > 0) {
                winner = player;
            }
        }

        int count = 0;
        if (winner != null) {
            for (Player player : getPlayers()) {
                if (player.getScore() == winner.getScore()) {
                    ++count;
                }
            }
        }

        return count > 1 ? null : winner;
    }

    public void stopGame() {
        getPlayers().forEach(Player::updateUserGlobalScore);
        room.gameOver();
    }

    public void setPlayerAnswer(Player player, String answer, int round) {
        if (playerAnswers.get(round) == null) {
            playerAnswers.put(round, new HashMap<>());
        }

        if (playerAnswers.get(round).get(player.getUserEmail()) == null) {
            playerAnswers.get(round).put(player.getUserEmail(), answer);
        }
    }

    public String getPlayerAnswer(Player player, int round) {
        if (playerAnswers.get(round) == null) {
            return null;
        }

        return playerAnswers.get(round).get(player.getUserEmail());
    }

    public Map<String, String> getPlayersAnswers(int round) {
        return playerAnswers.get(round);
    }

    public Set<Integer> getAskedQuestions() {
        return askedQuestions;
    }

    public void clearPlayerAnswers() {
        playerAnswers.clear();
    }
}
