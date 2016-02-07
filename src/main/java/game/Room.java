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

    private final long roomId;
    private final String gameMode;
    private final boolean isPrivate;
    private final Map<String, Player> playerByUser = new HashMap<>();
    private States state = States.WATING;
    GameSession session;
    AbstractGame game;
    final WebSocketService webSocketService;

    public Room(long roomId, WebSocketService webSocketService, String gameMode, boolean isPrivate) {
        this.roomId = roomId;
        this.gameMode = gameMode;
        state = States.WATING;
        this.webSocketService = webSocketService;
        this.isPrivate = isPrivate;
    }

    public States getState() {
        return state;
    }

    public Player getPlayerByUser(UserProfile user) {
        if (playerByUser.containsKey(user.getEmail())) {
            return playerByUser.get(user.getEmail());
        } else {
            return null;
        }
    }

    public void connectUser(UserProfile userProfile, GameWebSocket gameWebSocket) {
        Player player = new Player(userProfile);
        player.setConnection(gameWebSocket);
        webSocketService.notifyNewPlayerConnect(getPlayers(), player);
        playerByUser.put(userProfile.getEmail(), player);
        webSocketService.notifyAboutPlayersInRoom(player, getPlayers(), roomId);
        if (playerByUser.size() == ConfigGeneral.getMaxPlayersPerRoom()) {
            startGame();
        }
    }

    public void disconnectUser(UserProfile userProfile) {
        Player disconnectedPlayer = getPlayerByUser(userProfile);
        if (disconnectedPlayer != null) {
            playerByUser.remove(userProfile.getEmail());
            webSocketService.notifyPlayerDisconnect(getPlayers(), disconnectedPlayer);
            if (state == States.PLAYING && playerByUser.size() < ConfigGeneral.getMinPlayersPerRoom()) {
                game.gameOver();
            }
        }
    }

    public void gameOver() {
        state = States.WATING;
        playerByUser.clear();
    }

    public void startGame() {
        state = States.PLAYING;
        session = new GameSession(playerByUser, this);
        switch (gameMode) {
            case "BLITZ": game = new BlitzGame(webSocketService, session); break;
            case "MAPGAME": game = new MapGame(webSocketService, session); break;
            default: game = new BlitzGame(webSocketService, session);
        }
        game.play();
    }

    public Collection<Player> getPlayers() {
        return playerByUser.values();
    }

    public void setAnswer(UserProfile userProfile, String answer) {
        game.setPlayerAnswer(getPlayerByUser(userProfile), answer);
    }

    public long getRoomId() {
        return roomId;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setSelectedSegment(int segmentId) {
        game.setSelectedSegment(segmentId);
    }

}
