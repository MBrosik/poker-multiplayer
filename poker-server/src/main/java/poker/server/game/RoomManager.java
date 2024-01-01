package poker.server.game;

import poker.commons.socket.ReceiveData;
import poker.commons.socket.data_types.ActionType;
import poker.commons.socket.data_types.join_room.JoinRoomStatus;
import poker.server.socket.SessionData;
import poker.server.socket.SocketManager;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.HashMap;
import java.util.Map;

public class RoomManager {
    private RoomManager(){}

    private static final Map<Long, Room> rooms = new HashMap<>();

    public static void createRoom(SelectionKey key, SessionData playerData) throws IOException {
        Room room = new Room();
        rooms.put(room.getGameId(), room);

        room.addPlayer(playerData);
        playerData.setRoom(room);

        ReceiveData sendData = new ReceiveData(ActionType.CREATE_ROOM, room.getGameId());
        SocketManager.sendToClient(key, sendData);
    }

    public static void joinRoom(SelectionKey key, SessionData playerData, double id) throws IOException {
        var room = rooms.get((long) id);
        ReceiveData sendData;

        if (room == null || room.getRoomState() != Room.RoomState.PLAYER_GATHERING) {
            sendData = new ReceiveData(ActionType.JOIN_ROOM, JoinRoomStatus.NOT_EXISTS);
        } else if (room.isFull()) {
            sendData = new ReceiveData(ActionType.JOIN_ROOM, JoinRoomStatus.ROOM_IS_FULL);
        } else {
            room.addPlayer(playerData);
            playerData.setRoom(room);
            sendData = new ReceiveData(ActionType.JOIN_ROOM, JoinRoomStatus.ADDED);
        }
        SocketManager.sendToClient(key, sendData);
    }

    public static void tagPlayerAsReady(SessionData playerData) throws IOException {
        playerData.getPlayer().setReadyToPlay(true);
        var room = playerData.getRoom();

        if (room.areAllPlayersReady()) {
            startGame(room);
        }
    }

    public static void startGame(Room room) throws IOException {
        room.startGame();
    }
}
