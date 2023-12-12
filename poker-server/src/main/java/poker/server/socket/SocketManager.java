package poker.server.socket;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

import poker.commons.JSONManager;
import poker.commons.socket.ReceiveData;
import poker.server.game.RoomManager;

public class SocketManager {
    public static HashMap<SelectionKey, SessionData> sessions = new HashMap<>();

    public static void onConnect(SelectionKey key){
        if(!sessions.containsKey(key)){
            var session = new SessionData();
            session.setKey(key);
            sessions.put(key, session);
        }
    }

    public static void onMessage(CharBuffer buffer, SelectionKey key) throws IOException {
        onConnect(key);

        ReceiveData receiveData = JSONManager.jsonParse(buffer.toString());

//        System.out.println(":( "+receiveData.getData());

        switch (receiveData.getAction()){
            case CreateRoom -> RoomManager.createRoom(key, sessions.get(key));
            case JoinRoom -> RoomManager.joinRoom(key, sessions.get(key), (double) receiveData.getData());
            case ReadyToPlay -> RoomManager.tagPlayerAsReady(sessions.get(key));
        }
    }
    public static void sendToClient(SelectionKey key, ReceiveData data) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        client.write(JSONManager.jsonStringify(data));
    }
}
