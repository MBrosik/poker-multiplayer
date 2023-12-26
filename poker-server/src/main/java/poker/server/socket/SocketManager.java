package poker.server.socket;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

import poker.commons.JSONManager;
import poker.commons.MyLogger;
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
        SessionData session = sessions.get(key);

//        System.out.println(":( "+receiveData.getData());

        switch (receiveData.getAction()){
            case CreateRoom -> RoomManager.createRoom(key, session);
            case JoinRoom -> RoomManager.joinRoom(key, session, (double) receiveData.getData());
            case ReadyToPlay -> RoomManager.tagPlayerAsReady(session);
            case Bet -> session.getRoom().receiveBetFromPlayer(session.getPlayer(), receiveData);
            // case Check -> session.getRoom().receiveCheckFromPlayer(session.getPlayer(), receiveData);
            case Pass -> session.getRoom().receivePassFromPlayer(session.getPlayer(), receiveData);
        }
    }
    public static void sendToClient(SelectionKey key, ReceiveData data) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();

        client.write(JSONManager.jsonStringify(data));
    }
}
