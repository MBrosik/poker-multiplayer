package poker.client;

import poker.commons.MyLogger;
import poker.commons.MyScanner;
import poker.commons.socket.dataTypes.ActionType;
import poker.commons.socket.ReceiveData;
import poker.commons.socket.dataTypes.joinRoom.JoinRoomStatus;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        start();
        SocketClientManager.i.socketChannel.close();
    }

    private static void start() {
        UIManager.showHomeScreen();
        int num = MyScanner.getStreamInt();

        if (num == 1) createRoom();
        else if (num == 2) getRoom();
    }

    private static void createRoom() {
        ReceiveData receiveData = new ReceiveData(ActionType.CreateRoom, null);
        ReceiveData code = SocketClientManager.i.send(receiveData, true);

//        MyLogger.logln(code.getData());
        UIManager.showAddedRoomScreen((double) code.getData());
    }

    private static void getRoom() {
        long code = MyScanner.getStreamLong("Wpisz kod: ");
        ReceiveData receiveData = new ReceiveData(ActionType.JoinRoom, code);
        ReceiveData res = SocketClientManager.i.send(receiveData, true);

        JoinRoomStatus parsedData = JoinRoomStatus.valueOf((String) res.getData());

        if (parsedData == JoinRoomStatus.notExists) {
            MyLogger.logln("Dany pokój nie istnieje");
            start();
        } else if (parsedData == JoinRoomStatus.roomIsFull) {
            MyLogger.logln("Dany pokój jest pełny");
            start();
        }
        else if(parsedData == JoinRoomStatus.added){
            UIManager.showAddedToRoomScreen();
        }

    }
}