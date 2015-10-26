package game;

import model.UserProfile;
import utils.ConfigGeneral;
import websocket.GameWebSocket;
import websocket.WebSocketService;

import java.util.HashSet;
import java.util.Set;

/**
 * alex on 23.10.15.
 */
public class RoomManager {
    private final Set<Room> rooms = new HashSet<>();
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
}
