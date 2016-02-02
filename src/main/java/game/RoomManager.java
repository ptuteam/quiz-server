package game;

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

    private Room createRoom(String gameMode, boolean isPrivate) {
        Room room = new Room(roomIdIncrement.incrementAndGet(), webSocketService, gameMode, isPrivate);
        rooms.add(room);
        return room;
    }

    public Room connectUser(UserProfile user, GameWebSocket socket, String gameMode, long roomId, boolean isRoomPrivate) {
        Room room;

        if (roomId > 0) {
            room = getNotPlayingRoom(roomId);
        } else if (isRoomPrivate) {
            room = getFreePrivateRoom(gameMode);
        } else {
            room = getNotPlayingPublicRoom(gameMode);
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

    @SuppressWarnings("OverlyComplexBooleanExpression")
    private Room getFreePrivateRoom(String gameMode) {
        for (Room room : rooms) {
            if (room.getGameMode().equals(gameMode) && room.isPrivate() && room.getState() == Room.States.WATING && room.getPlayers().isEmpty()) {
                return room;
            }
        }

        if (rooms.size() < ConfigGeneral.getRoomsCount()) {
            return createRoom(gameMode, true);
        } else {
            return null;
        }
    }

    @SuppressWarnings("OverlyComplexBooleanExpression")
    private Room getNotPlayingPublicRoom(String gameMode) {
        for (Room room : rooms) {
            if (room.getGameMode().equals(gameMode) && !room.isPrivate() && room.getState() == Room.States.WATING && room.getPlayers().size() < ConfigGeneral.getMaxPlayersPerRoom()) {
                return room;
            }
        }

        if (rooms.size() < ConfigGeneral.getRoomsCount()) {
            return createRoom(gameMode, false);
        } else {
            return null;
        }
    }
}
