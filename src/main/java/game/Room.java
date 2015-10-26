package game;

import model.UserProfile;
import utils.ConfigGeneral;
import websocket.GameWebSocket;
import websocket.WebSocketService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * alex on 23.10.15.
 */
public class Room {

    public enum States {
        WATING,
        PLAYING
    }

    private final Map<UserProfile, Player> playerByUser = new HashMap<>();
    private States state = States.WATING;
    GameSession session;
    GameField gameField;
    final WebSocketService webSocketService;

    public Room(WebSocketService webSocketService) {
        state = States.WATING;
        this.webSocketService = webSocketService;
    }

    public States getState() {
        return state;
    }

    public Player getPlayerByUser(UserProfile user) {
        if (playerByUser.containsKey(user)) {
            return playerByUser.get(user);
        } else {
            return null;
        }
    }

    public void connectUser(UserProfile userProfile, GameWebSocket gameWebSocket) {
        Player player = new Player(userProfile);
        player.setConnection(gameWebSocket);
        playerByUser.put(userProfile, player);
        if (playerByUser.size() == ConfigGeneral.getMaxPlayersPerRoom()) {
            startGame();
        }
    }

    public void disconnectUser(UserProfile userProfile) {
        webSocketService.notifyPlayerDisconnect(getPlayers(), getPlayerByUser(userProfile));
        playerByUser.remove(userProfile);
        if (playerByUser.size() <= 1) {
            gameField.gameOver();
        }
    }

    public void gameOver() {
        state = States.WATING;
        playerByUser.clear();
    }

    public void startGame() {
        state = States.PLAYING;
        session = new GameSession(playerByUser, this);
        gameField = new GameFieldImpl(webSocketService, session);
        gameField.play();
    }

    public Collection<Player> getPlayers() {
        return playerByUser.values();
    }

    public void checkAnswer(UserProfile userProfile, String answer) {
        gameField.checkPlayerAnswer(getPlayerByUser(userProfile), answer);
    }
}