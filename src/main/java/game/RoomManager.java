package game;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import model.UserProfile;
import utils.ConfigGeneral;
import websocket.GameWebSocket;
import websocket.WebSocketService;

import java.util.ArrayList;
import java.util.List;

/**
 * alex on 23.10.15.
 */
public class RoomManager {
    private final List<Room> rooms = new ArrayList<>();
    private final WebSocketService webSocketService;

    public RoomManager(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    private Room createRoom() {
        Room room = new Room(webSocketService);
        rooms.add(room);
        return room;
    }

    public Room connectUser(UserProfile user, GameWebSocket socket) {
        Room room = getFreeRoom();
        if (room == null) {
            return null;
        }
        room.connectUser(user, socket);
        return room;
    }

    private Room getFreeRoom() {
        for (Room room : rooms) {
            if (room.getState() == Room.States.WATING && room.getPlayers().size() < ConfigGeneral.getMaxPlayersPerRoom()) {
                return room;
            }
        }

        if (rooms.size() < ConfigGeneral.getRoomsCount()) {
            return createRoom();
        } else {
            return null;
        }
    }

    public JsonObject getRoomsInforamtion() {
        JsonObject jsonObject = new JsonObject();
        JsonArray roomsArray = new JsonArray();
        jsonObject.add("rooms", roomsArray);

        rooms.stream().filter(room -> room.getState() == Room.States.WATING).forEach(room -> {
            JsonObject roomObject = new JsonObject();
            roomObject.addProperty("id", "someId");
            roomObject.addProperty("playersCount", room.getPlayersCount());
            roomsArray.add(roomObject);
        });

        return jsonObject;
    }
}
