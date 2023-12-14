package poker.server.game;

import poker.commons.socket.ReceiveData;
import poker.commons.socket.dataTypes.ActionType;
import poker.commons.socket.dataTypes.joinRoom.JoinRoomStatus;
import poker.server.socket.SessionData;
import poker.server.socket.SocketManager;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.HashMap;

public class RoomManager {
    public static HashMap<Long, Room> rooms = new HashMap<>();

    public static void createRoom(SelectionKey key, SessionData playerData) throws IOException {
        Room room = new Room();
        rooms.put(room.getGameId(), room);

        room.addPlayer(playerData);
        playerData.setRoom(room);

        ReceiveData sendData = new ReceiveData(ActionType.CreateRoom, room.getGameId());
        SocketManager.sendToClient(key, sendData);

//        var test1 = new Gson();

//        System.out.println(test1.toJson(rooms.keySet()));
    }

    public static void joinRoom(SelectionKey key, SessionData playerData, double id) throws IOException {
        System.out.println(id);
        var room = rooms.get((long) id);
        ReceiveData sendData;

        if (room == null) {
            sendData = new ReceiveData(ActionType.JoinRoom, JoinRoomStatus.notExists);
        } else if (room.isFull()) {
            sendData = new ReceiveData(ActionType.JoinRoom, JoinRoomStatus.roomIsFull);
        } else {
            room.addPlayer(playerData);
            playerData.setRoom(room);
            sendData = new ReceiveData(ActionType.JoinRoom, JoinRoomStatus.added);
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
        room.gameLoop();
    }

}
