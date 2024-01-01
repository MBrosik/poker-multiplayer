package poker.server.socket;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

import poker.commons.JSONManager;
import poker.commons.MyLogger;
import poker.commons.socket.ReceiveData;
import poker.server.game.RoomManager;

public class SocketManager {
    private SocketManager(){}

    private static final Map<SelectionKey, SessionData> sessions = new HashMap<>();

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

        switch (receiveData.getAction()){
            case CREATE_ROOM -> RoomManager.createRoom(key, session);
            case JOIN_ROOM -> RoomManager.joinRoom(key, session, (double) receiveData.getData());
            case READY_TO_PLAY -> RoomManager.tagPlayerAsReady(session);
            case BET -> session.getRoom().receiveBetFromPlayer(session.getPlayer(), receiveData);
            case PASS -> session.getRoom().receivePassFromPlayer(session.getPlayer());
            case READY_FOR_NEXT_ROUND -> session.getRoom().infoFromPlayerAboutNextTurn(session, receiveData);
            default -> MyLogger.logln("No routing");
        }
    }
    public static void sendToClient(SelectionKey key, ReceiveData data) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();

        client.write(JSONManager.jsonStringify(data));
    }
}
