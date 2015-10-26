package game;

import model.UserProfile;

import java.util.*;

/**
 * alex on 22.10.15.
 */
public class GameSession {
    private final long startTime;
    private final Room room;
    private Map<UserProfile, Player> players = new HashMap<>();

    public GameSession(Map<UserProfile, Player> players, Room room) {
        startTime = new Date().getTime();
        this.players = players;
        this.room = room;
    }

    public Set<Player> getPlayers() {
        return new HashSet<>(players.values());
    }

    public void increaseScore(UserProfile user, int value) {
        players.get(user).increaseScore(value);
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

        return winner != null ? winner : null;
    }

    public void stopGame() {
        getPlayers().forEach(Player::updateUserGlobalScore);
        room.gameOver();
    }
}
