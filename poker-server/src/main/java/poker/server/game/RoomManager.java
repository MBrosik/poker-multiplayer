package poker.server.game;

import com.google.gson.Gson;
import poker.commons.socket.dataTypes.ActionType;
import poker.commons.socket.dataTypes.joinRoom.JoinRoomStatus;
import poker.commons.socket.ReceiveData;
import poker.server.socket.SessionData;
import poker.server.socket.SocketManager;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.HashMap;

public class RoomManager {
    public static HashMap<Long,Room> rooms = new HashMap<>();

    public static void createRoom(SelectionKey key, SessionData playerData) throws IOException {
        Room room = new Room();
        rooms.put(room.getGameId(), room);

        //room;
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

        if(room == null){
            sendData = new ReceiveData(ActionType.JoinRoom, JoinRoomStatus.notExists);
        }
        else if(room.isFull()){
            sendData = new ReceiveData(ActionType.JoinRoom, JoinRoomStatus.roomIsFull);
        }
        else{
            room.addPlayer(playerData);
            sendData = new ReceiveData(ActionType.JoinRoom, JoinRoomStatus.added);
        }
        SocketManager.sendToClient(key, sendData);
    }
}
