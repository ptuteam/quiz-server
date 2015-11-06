package websocket;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import game.Player;
import game.Room;
import game.RoomManager;
import model.UserProfile;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;


/**
 * alex on 24.10.15.
 */
@WebSocket
public class GameWebSocket {
    private final UserProfile userProfile;
    private Session mySession;
    private Room room;
    private final RoomManager roomManager;

    public GameWebSocket(UserProfile userProfile, RoomManager roomManager) {
        this.userProfile = userProfile;
        this.roomManager = roomManager;
    }

    @SuppressWarnings("unused")
    @OnWebSocketConnect
    public void onOpen(Session session) {
        mySession = session;
        room = roomManager.connectUser(userProfile, this);

        if (room == null) {
            onNoEmptyRooms();
        }
    }

    @SuppressWarnings("unused")
    @OnWebSocketMessage
    public void onMessage(String data) {
        JsonObject message = new JsonParser().parse(data).getAsJsonObject();
        try {
            if (room.getState() == Room.States.PLAYING) {
                switch (message.get("code").getAsInt()) {
                    case 6:
                        room.checkAnswer(userProfile, message.get("answer").getAsString());
                        break;
                    default:
                        break;
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        if (room != null) {
            room.disconnectUser(userProfile);
        }
    }

    @SuppressWarnings("unused")
    @OnWebSocketError
    public void onError(Throwable error) {
        error.printStackTrace();
        if (room != null) {
            room.disconnectUser(userProfile);
        }
    }

    public void onStartGame(Collection<Player> players) {
        try {
            JsonObject message = new JsonObject();
            message.addProperty("code", "1");
            message.addProperty("description", "start");

            JsonArray usersArray = new JsonArray();
            message.add("players", usersArray);

            for (Player player : players) {
                UserProfile user = player.getUserProfile();

                JsonObject userObject = new JsonObject();
                userObject.addProperty("email", user.getEmail());
                userObject.addProperty("first_name", user.getFirstName());
                userObject.addProperty("last_name", user.getLastName());
                userObject.addProperty("avatar", user.getAvatarUrl());
                userObject.addProperty("score", player.getScore());

                usersArray.add(userObject);
            }
            mySession.getRemote().sendString(message.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onNewScores(Map<String, Integer> scoresMap) {
        try {
            JsonObject message = new JsonObject();
            message.addProperty("code", "2");
            message.addProperty("description", "new players scores");
            JsonArray playersArray = new JsonArray();
            message.add("players", playersArray);
            for (Map.Entry<String, Integer> entry : scoresMap.entrySet()) {
                JsonObject player = new JsonObject();
                player.addProperty("email", entry.getKey());
                player.addProperty("score", entry.getValue());
                playersArray.add(player);
            }
            mySession.getRemote().sendString(message.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onGameOver(Player winner) {
        try {
            JsonObject message = new JsonObject();
            message.addProperty("code", "3");
            message.addProperty("description", "finish");
            message.addProperty("winner", winner.getUserEmail());
            mySession.getRemote().sendString(message.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onEnemyDisconnect(Player enemy) {
        try {
            JsonObject message = new JsonObject();
            message.addProperty("code", "4");
            message.addProperty("description", "player disconnect");
            message.addProperty("player", enemy.getUserEmail());
            mySession.getRemote().sendString(message.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onNewQuestionAsk(JsonObject questionObject) {
        try {
            questionObject.addProperty("code", "5");
            questionObject.addProperty("description", "new question");
            mySession.getRemote().sendString(questionObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onNoEmptyRooms() {
        try {
            JsonObject message = new JsonObject();
            message.addProperty("code", "7");
            message.addProperty("description", "no empty rooms");
            mySession.getRemote().sendString(message.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onNewPlayerConnect(Player newPlayer) {
        try {
            JsonObject message = new JsonObject();
            message.addProperty("code", "8");
            message.addProperty("description", "new player connect");
            JsonObject playerObject = new JsonObject();
            playerObject.addProperty("email", newPlayer.getUserProfile().getEmail());
            playerObject.addProperty("first_name", newPlayer.getUserProfile().getFirstName());
            playerObject.addProperty("last_name", newPlayer.getUserProfile().getLastName());
            playerObject.addProperty("avatar", newPlayer.getUserProfile().getAvatarUrl());
            playerObject.addProperty("score", newPlayer.getScore());
            message.add("player", playerObject);
            mySession.getRemote().sendString(message.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onNewRoundStart(int round) {
        try {
            JsonObject message = new JsonObject();
            message.addProperty("code", "9");
            message.addProperty("description", "new round start");
            message.addProperty("round", round);
            mySession.getRemote().sendString(message.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onCorrectAnswer(boolean correct) {
        try {
            JsonObject message = new JsonObject();
            message.addProperty("code", "10");
            message.addProperty("description", "is answer correct?");
            message.addProperty("correct", correct);
            mySession.getRemote().sendString(message.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listPlayersInRoom(Collection<Player> players) {
        try {
            JsonObject message = new JsonObject();
            message.addProperty("code", "11");
            message.addProperty("description", "players in room");

            JsonArray usersArray = new JsonArray();
            message.add("players", usersArray);

            for (Player player : players) {
                UserProfile user = player.getUserProfile();

                JsonObject userObject = new JsonObject();
                userObject.addProperty("email", user.getEmail());
                userObject.addProperty("first_name", user.getFirstName());
                userObject.addProperty("last_name", user.getLastName());
                userObject.addProperty("avatar", user.getAvatarUrl());
                userObject.addProperty("score", player.getScore());

                usersArray.add(userObject);
            }
            mySession.getRemote().sendString(message.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
