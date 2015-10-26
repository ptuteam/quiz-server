package game;

import model.UserProfile;
import websocket.GameWebSocket;

/**
 * alex on 23.10.15.
 */
public class Player {
    private int score;
    private final UserProfile userProfile;
    private GameWebSocket connection;

    public Player(UserProfile userProfile) {
        this.userProfile = userProfile;
        score = 0;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public String getUserEmail() {
        return userProfile.getEmail();
    }

    public void setConnection(GameWebSocket webSocket) {
        connection = webSocket;
    }

    public GameWebSocket getConnection() {
        return connection;
    }

    public int getScore() {
        return score;
    }

    public void increaseScore(int value) {
        this.score += value;
    }

    public void updateUserGlobalScore() {
        userProfile.setScore(userProfile.getScore() + score);
    }
}
