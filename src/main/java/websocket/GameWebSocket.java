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
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.util.Collection;


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

    public void onStartGame(Collection<Player> players) {
        try {
            JsonObject jsonStart = new JsonObject();
            jsonStart.addProperty("code", "1");
            jsonStart.addProperty("description", "start");

            JsonArray usersArray = new JsonArray();
            jsonStart.add("players", usersArray);

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
            mySession.getRemote().sendString(jsonStart.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onGameOver(Player winner) {
        try {
            JsonObject jsonStart = new JsonObject();
            jsonStart.addProperty("code", "3");
            jsonStart.addProperty("description", "finish");
            jsonStart.addProperty("winner", winner.getUserEmail());
            mySession.getRemote().sendString(jsonStart.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    @OnWebSocketMessage
    public void onMessage(String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        switch (jsonObject.get("code").getAsInt()) {
            case 6:
                room.checkAnswer(userProfile, jsonObject.get("answer").getAsString());
                break;
            default:
                break;
        }
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

    public void onNoEmptyRooms() {
        try {
            JsonObject jsonStart = new JsonObject();
            jsonStart.addProperty("code", "7");
            jsonStart.addProperty("description", "No empty rooms");
            mySession.getRemote().sendString(jsonStart.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onChangeScore(Player player) {
        try {
            JsonObject jsonStart = new JsonObject();
            jsonStart.addProperty("code", "2");
            jsonStart.addProperty("description", "player onChange score");
            jsonStart.addProperty("player", player.getUserEmail());
            jsonStart.addProperty("new_score", player.getScore());
            mySession.getRemote().sendString(jsonStart.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onEnemyDisconnect(Player enemy) {
        try {
            JsonObject jsonStart = new JsonObject();
            jsonStart.addProperty("code", "4");
            jsonStart.addProperty("description", "player disconnect");
            jsonStart.addProperty("player", enemy.getUserEmail());
            mySession.getRemote().sendString(jsonStart.toString());
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

    @SuppressWarnings("unused")
    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        room.disconnectUser(userProfile);
    }
}
