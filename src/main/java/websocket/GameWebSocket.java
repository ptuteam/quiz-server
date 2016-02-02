package websocket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import game.*;
import model.UserProfile;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketException;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.messages.Message;
import websocket.messages.NewScoresMessage;
import websocket.messages.SimpleMessage;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * alex on 24.10.15.
 */
@WebSocket
public class GameWebSocket {
    private static final int CODE_START = 1;
    private static final int CODE_UPDATE_SCORES = 2;
    private static final int CODE_GAME_OVER = 3;
    private static final int CODE_ENEMY_DISCONNECT = 4;
    private static final int CODE_NEW_QUESTION = 5;
    private static final int CODE_PLAYER_ANSWER = 6;
    private static final int CODE_NO_EMPTY_ROOMS = 7;
    private static final int CODE_NEW_PLAYER_CONNECT = 8;
    private static final int CODE_NEW_ROUND_START = 9;
    private static final int CODE_LIST_PLAYERS_ANSWERS = 10;
    private static final int CODE_LIST_PLAYERS_IN_ROOM = 11;
    private static final int CODE_PLAYER_TURN_START = 12;
    private static final int CODE_PLAYER_SELECT_SEGMENT = 13;
    private static final int CODE_SELECTED_SEGMENT = 14;
    private static final int CODE_INCORRECT_SELECT_SEGMENT = 15;
    private static final int CODE_PLAYER_TURN_FINISH = 16;
    private static final int CODE_ROUND_FINISH = 17;

    private static final int ROOM_PRIVATE_TYPE = 1;

    private final UserProfile userProfile;
    private Session mySession;
    private Room room;
    private final RoomManager roomManager;
    private final String mode;
    private final long connectToRoomId;
    private final int roomType;

    public GameWebSocket(UserProfile userProfile, RoomManager roomManager, String mode, long connectToRoomId, int roomType) {
        this.userProfile = userProfile;
        this.roomManager = roomManager;
        this.mode = mode == null ?  GameMode.BLITZ.name() : mode;
        this.connectToRoomId = connectToRoomId;
        this.roomType = roomType;
    }

    @SuppressWarnings("unused")
    @OnWebSocketConnect
    public void onOpen(Session session) {
        mySession = session;
        room = roomManager.connectUser(userProfile, this, mode, connectToRoomId, roomType == ROOM_PRIVATE_TYPE);
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
                    case CODE_PLAYER_ANSWER:
                        room.setAnswer(userProfile, message.get("answer").getAsString());
                        break;
                    case CODE_PLAYER_SELECT_SEGMENT:
                        room.setSelectedSegment(message.get("segmentId").getAsInt());
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

    private void sendMessage(Message message) {
        try {
            mySession.getRemote().sendString(message.getAsString());
        } catch (IOException | WebSocketException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("Duplicates")
    public void onStartBlitzGame(Player player, Collection<Player> opponents) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("code", CODE_START);
        parameters.put("description", "start");
        parameters.put("mode", GameMode.BLITZ);
        parameters.put("player", player);
        parameters.put("opponents", opponents);

        sendMessage(new SimpleMessage(parameters));
    }

    public void onNewScores(Player player, Collection<Player> opponents) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("code", CODE_UPDATE_SCORES);
        parameters.put("description", "new players scores");

        sendMessage(new NewScoresMessage(parameters, player, opponents));
    }

    @SuppressWarnings("Duplicates")
    public void onGameOver(Player winner) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("code", CODE_GAME_OVER);
        parameters.put("description", "finish");
        parameters.put("winner", winner.getUserEmail());

        sendMessage(new SimpleMessage(parameters));
    }

    @SuppressWarnings("Duplicates")
    public void onEnemyDisconnect(Player enemy) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("code", CODE_ENEMY_DISCONNECT);
        parameters.put("description", "player disconnect");
        parameters.put("player", enemy.getUserEmail());

        sendMessage(new SimpleMessage(parameters));
    }

    @SuppressWarnings("Duplicates")
    public void onNewQuestionAsk(Question question) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("code", CODE_NEW_QUESTION);
        parameters.put("description", "new question");
        parameters.put("question", question);
        parameters.put("type", question.getType());

        sendMessage(new SimpleMessage(parameters));
    }

    public void onNoEmptyRooms() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("code", CODE_NO_EMPTY_ROOMS);
        parameters.put("description", "no empty rooms");

        sendMessage(new SimpleMessage(parameters));
    }

    public void onNewPlayerConnect(Player newPlayer) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("code", CODE_NEW_PLAYER_CONNECT);
        parameters.put("description", "new player connect");
        parameters.put("player", newPlayer.getUserProfile());

        sendMessage(new SimpleMessage(parameters));
    }

    @SuppressWarnings("Duplicates")
    public void onNewRoundStart(int round) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("code", CODE_NEW_ROUND_START);
        parameters.put("description", "new round start");
        parameters.put("round", round);

        sendMessage(new SimpleMessage(parameters));
    }

    public void listPlayersInRoom(Collection<Player> players, long roomId) {
        List<UserProfile> users = players.stream().map(Player::getUserProfile).collect(Collectors.toList());

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("code", CODE_LIST_PLAYERS_IN_ROOM);
        parameters.put("description", "players in room");
        parameters.put("players", users);
        parameters.put("roomId", roomId);

        sendMessage(new SimpleMessage(parameters));
    }

    @SuppressWarnings("Duplicates")
    public void listPlayersAnswers(String correctAnswer, Map<String, String> playersAnswers) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("code", CODE_LIST_PLAYERS_ANSWERS);
        parameters.put("description", "players answers");
        parameters.put("answers", playersAnswers);
        parameters.put("correct", correctAnswer);

        sendMessage(new SimpleMessage(parameters));
    }

    public void onStartMapGame(Player player, Collection<Player> opponents, Collection<MapSegment> mapData) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("code", CODE_START);
        parameters.put("description", "start");
        parameters.put("mode", GameMode.MAPGAME);
        parameters.put("player", player);
        parameters.put("opponents", opponents);
        parameters.put("map", mapData);

        sendMessage(new SimpleMessage(parameters));
    }

    public void onPlayerTurnStart(Player player, Collection<Integer> allowableMove) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("code", CODE_PLAYER_TURN_START);
        parameters.put("player", player.getUserEmail());
        parameters.put("allowableMove", allowableMove);
        parameters.put("description", "player turn start");

        sendMessage(new SimpleMessage(parameters));
    }

    @SuppressWarnings("Duplicates")
    public void onPlayerSelectedSegment(int segmentId, String owner) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("code", CODE_SELECTED_SEGMENT);
        parameters.put("segmentId", segmentId);
        parameters.put("owner", owner);
        parameters.put("description", "selected segment");

        sendMessage(new SimpleMessage(parameters));
    }

    public void onIncorrectSelectSegment() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("code", CODE_INCORRECT_SELECT_SEGMENT);
        parameters.put("description", "incorrect select segment");

        sendMessage(new SimpleMessage(parameters));
    }

    public void onPlayerTurnFinish(Player player, Integer invadedSegment) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("code", CODE_PLAYER_TURN_FINISH);
        parameters.put("player", player.getUserEmail());
        parameters.put("score", player.getScore());
        parameters.put("invadedSegment", invadedSegment);
        parameters.put("description", "player turn finish");

        sendMessage(new SimpleMessage(parameters));
    }

    public void onRoundFinish() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("code", CODE_ROUND_FINISH);
        parameters.put("description", "round finish");

        sendMessage(new SimpleMessage(parameters));
    }
}
