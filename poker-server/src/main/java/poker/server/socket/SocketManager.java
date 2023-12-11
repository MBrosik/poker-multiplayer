package poker.server.socket;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.HashMap;

import poker.commons.JSONManager;
import poker.commons.socket.ReceiveData;
import poker.server.game.RoomManager;

public class SocketManager {
    public static HashMap<SelectionKey, SessionData> sessions = new HashMap<>();

    public static void onConnect(SelectionKey key){
        if(!sessions.containsKey(key)){
            sessions.put(key, new SessionData());
        }
    }

    public static void onMessage(CharBuffer buffer, SelectionKey key) throws IOException {
        onConnect(key);

        ReceiveData receiveData = JSONManager.jsonParse(buffer);

        System.out.println(":( "+receiveData.getData());

        switch (receiveData.getAction()){
            case CreateRoom -> RoomManager.createRoom(key, sessions.get(key));
        }
    }
}
