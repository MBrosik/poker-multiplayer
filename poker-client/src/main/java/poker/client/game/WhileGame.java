package poker.client.game;

import poker.client.SocketClientManager;
import poker.client.UIManager;
import poker.commons.JSONManager;
import poker.commons.MyLogger;
import poker.commons.MyScanner;
import poker.commons.socket.ReceiveData;
import poker.commons.socket.dataTypes.ActionType;
import poker.commons.socket.dataTypes.whileGame.GameData;
import poker.commons.socket.dataTypes.whileGame.PlayerType;

import java.io.IOException;

public class WhileGame {
    public static void start(ReceiveData receiveData) throws IOException {
        GameData data = JSONManager.reparseJson(receiveData.getData(), GameData.class);
        UIManager.showGameIsStartedScreen(data.getPlayerType(), data.getMoney());

        if(data.getPlayerType() == PlayerType.SmallBlind){
            smallBlindBet();
        }
        else{
            waitingForSmallBlindBet();
        }
    }
    public static void smallBlindBet(){
        MyLogger.logLineSep();
        int bet = MyScanner.getStreamInt("Podaj stawkę:");

        var data = new ReceiveData(ActionType.Bet, bet);
        var response = SocketClientManager.i.send(data, true);

        System.out.println(JSONManager.jsonStringify(response));

    }
    public static void waitingForSmallBlindBet() throws IOException {
        var data =SocketClientManager.i.getDataFromServer();
        System.out.println(JSONManager.jsonStringify(data));
    }
}
