package poker.server.game;

import poker.commons.JSONManager;
import poker.commons.socket.ActionType;
import poker.commons.socket.ReceiveData;
import poker.server.socket.SessionData;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RoomManager {
    public static HashMap<Long,Room> rooms = new HashMap<>();

    public static void createRoom(SelectionKey key, SessionData playerData) throws IOException {
        Room room = new Room();
        rooms.put(room.getGameId(), room);

        playerData.setRoom(room);

        SocketChannel client = (SocketChannel) key.channel();
        ReceiveData sendData = new ReceiveData();

        sendData.setAction(ActionType.JoinRoom);
        sendData.setData(room.getGameId());


        client.write(JSONManager.jsonStringify(sendData));
    }
}
