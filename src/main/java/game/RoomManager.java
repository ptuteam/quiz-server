package game;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import model.UserProfile;
import utils.ConfigGeneral;
import websocket.GameWebSocket;
import websocket.WebSocketService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * alex on 23.10.15.
 */
public class RoomManager {
    private final List<Room> rooms = new ArrayList<>();
    private final WebSocketService webSocketService;
    private final AtomicLong roomIdIncrement = new AtomicLong();

    public RoomManager(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    private Room createRoom(boolean isPrivate) {
        Room room = new Room(roomIdIncrement.incrementAndGet(), webSocketService, isPrivate);
        rooms.add(room);
        return room;
    }

    public Room connectUser(UserProfile user, GameWebSocket socket, long roomId, boolean isRoomPrivate) {
        Room room;

        if (roomId > 0) {
            room = getNotPlayingRoom(roomId);
        } else if (isRoomPrivate) {
            room = getFreePrivateRoom();
        } else {
            room = getNotPlayingPublicRoom();
        }

        if (room == null) {
            return null;
        }
        room.connectUser(user, socket);
        return room;
    }

    private Room getNotPlayingRoom(long roomId) {
        for (Room room : rooms) {
            if (Objects.equals(room.getRoomId(), roomId) && room.getState() == Room.States.WATING && room.getPlayers().size() < ConfigGeneral.getMaxPlayersPerRoom()) {
                return room;
            }
        }

        return null;
    }

    private Room getFreePrivateRoom() {
        for (Room room : rooms) {
            if (room.isPrivate() && room.getState() == Room.States.WATING && room.getPlayers().isEmpty()) {
                return room;
            }
        }

        if (rooms.size() < ConfigGeneral.getRoomsCount()) {
            return createRoom(true);
        } else {
            return null;
        }
    }

    private Room getNotPlayingPublicRoom() {
        for (Room room : rooms) {
            if (!room.isPrivate() && room.getState() == Room.States.WATING && room.getPlayers().size() < ConfigGeneral.getMaxPlayersPerRoom()) {
                return room;
            }
        }

        if (rooms.size() < ConfigGeneral.getRoomsCount()) {
            return createRoom(false);
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
            roomObject.addProperty("id", room.getRoomId());
            roomObject.addProperty("playersCount", room.getPlayersCount());
            roomsArray.add(roomObject);
        });

        return jsonObject;
    }
}
