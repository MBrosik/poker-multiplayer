package poker.client.game;

import poker.client.SocketClientManager;
import poker.client.UIManager;
import poker.commons.MyLogger;
import poker.commons.MyScanner;
import poker.commons.socket.ReceiveData;
import poker.commons.socket.data_types.ActionType;
import poker.commons.socket.data_types.join_room.JoinRoomStatus;

import java.io.IOException;


public class BeforeGame {

    private BeforeGame(){}

    public static void start() throws IOException {
        UIManager.showHomeScreen();
        int num = MyScanner.getStreamInt();

        if (num == 1) createRoom();
        else if (num == 2) getRoom();
    }

    private static void createRoom() throws IOException {
        ReceiveData receiveData = new ReceiveData(ActionType.CREATE_ROOM, null);
        ReceiveData code = SocketClientManager.i.send(receiveData, true);

        UIManager.showAddedRoomScreen((double) code.getData());
        confirmReadyToPlay();
    }

    private static void getRoom() throws IOException {
        MyLogger.logLineSep();
        long code = MyScanner.getStreamLong("Wpisz kod: ");
        ReceiveData receiveData = new ReceiveData(ActionType.JOIN_ROOM, code);
        ReceiveData res = SocketClientManager.i.send(receiveData, true);

        JoinRoomStatus parsedData = JoinRoomStatus.valueOf((String) res.getData());

        if (parsedData == JoinRoomStatus.NOT_EXISTS) {
            MyLogger.logln("Dany pokój nie istnieje");
            start();
        } else if (parsedData == JoinRoomStatus.ROOM_IS_FULL) {
            MyLogger.logln("Dany pokój jest pełny");
            start();
        }
        else if(parsedData == JoinRoomStatus.ADDED){
            UIManager.showAddedToRoomScreen();
            confirmReadyToPlay();
        }
    }

    private static void confirmReadyToPlay() throws IOException {
        while(true){
            String num = MyScanner.getStreamString();

            if(num.equals("1")){
                var data = new ReceiveData(ActionType.READY_TO_PLAY, null);
                SocketClientManager.i.send(data, false);
                MyLogger.logln("Zostałeś oznaczony jako gotowy do gry. \nZa chwilę gra się rozpocznie.");
                break;
            }
            else{
                MyLogger.logln("Zła opcja. Wybierz ponownie");
            }
        }

        var receivedData = SocketClientManager.i.getDataFromServer();

        if(receivedData.getAction() == ActionType.START_GAME_INFO){
            UIManager.showGameIsStartedScreen();
            WhileGame.start(receivedData);
        }
    }
}
