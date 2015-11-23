package game;

import model.UserProfile;
import utils.AccountService;
import utils.AccountServiceImpl;
import websocket.GameWebSocket;

/**
 * alex on 23.10.15.
 */
public class Player {
    private int score;
    private final UserProfile userProfile;
    private GameWebSocket connection;
    private final AccountService accountService;

    public Player(UserProfile userProfile) {
        this.userProfile = userProfile;
        score = 0;
        accountService = new AccountServiceImpl();
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
        accountService.updateUserScore(userProfile, userProfile.getScore() + score);
    }
}
