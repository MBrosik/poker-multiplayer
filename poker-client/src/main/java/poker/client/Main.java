package poker.client;

import poker.commons.MyLogger;
import poker.commons.MyScanner;
import poker.commons.socket.ActionType;
import poker.commons.socket.ReceiveData;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        start();
        SocketClientManager.i.socketChannel.close();
    }
    private static void start(){
        UIManager.showHomeScreen();
        int num = MyScanner.getStreamInt();

        if(num == 1) createRoom();
        else if(num == 2) getRoom();
    }

    private static void createRoom(){
        ReceiveData receiveData = new ReceiveData();
        receiveData.setAction(ActionType.CreateRoom);
        receiveData.setData(null);

        String code = SocketClientManager.i.send(receiveData, true);

        MyLogger.logln(code);
    }

    private static void getRoom(){

    }
}