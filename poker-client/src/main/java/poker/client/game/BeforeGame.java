package poker.client.game;

import poker.client.SocketClientManager;
import poker.client.UIManager;
import poker.commons.MyLogger;
import poker.commons.MyScanner;
import poker.commons.socket.ReceiveData;
import poker.commons.socket.dataTypes.ActionType;
import poker.commons.socket.dataTypes.joinRoom.JoinRoomStatus;

import java.io.IOException;

public class BeforeGame {
    public static void start() throws IOException {
        UIManager.showHomeScreen();
        int num = MyScanner.getStreamInt();

        if (num == 1) createRoom();
        else if (num == 2) getRoom();
    }

    private static void createRoom() throws IOException {
        ReceiveData receiveData = new ReceiveData(ActionType.CreateRoom, null);
        ReceiveData code = SocketClientManager.i.send(receiveData, true);

//        MyLogger.logln(code.getData());
        UIManager.showAddedRoomScreen((double) code.getData());
        confirmReadyToPlay();
    }

    private static void getRoom() throws IOException {
        MyLogger.logLineSep();
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
            confirmReadyToPlay();
        }
    }

    private static void confirmReadyToPlay() throws IOException {
        while(true){
            String num = MyScanner.getStreamString();

            if(num.equals("1")){
                var data = new ReceiveData(ActionType.ReadyToPlay, null);
                SocketClientManager.i.send(data, false);
                MyLogger.logln("Zostałeś oznaczony jako gotowy do gry. \nZa chwilę gra się rozpocznie.");
                break;
            }
            else{
                MyLogger.logln("Zła opcja. Wybierz ponownie");
            }
        }

        var receivedData = SocketClientManager.i.getDataFromServer();

        if(receivedData.getAction() == ActionType.StartGameInfo){
            WhileGame.start(receivedData);
        }

//        gameLoop(true);
    }
}
