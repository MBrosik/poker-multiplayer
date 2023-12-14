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
        UIManager.showGameIsStartedScreen();
        UIManager.showSmallBlindTurnScreen(data.getPlayerType(), data.getMoney());

        if (data.getPlayerType() == PlayerType.SmallBlind) {
            myBetAsSmallBlind();
        } else {
            waitingForSmallBlindBet();
        }
    }

    public static void myBetAsSmallBlind() throws IOException {
        var response = myBet();

        afterSmallBlindTurn(response);
    }

    private static ReceiveData myBet() {
        MyLogger.logLineSep();
        int bet = MyScanner.getStreamInt("Podaj stawkę:");

        var data = new ReceiveData(ActionType.Bet, bet);
        return SocketClientManager.i.send(data, true);
    }

    public static void waitingForSmallBlindBet() throws IOException {
        var response = SocketClientManager.i.getDataFromServer();

        var data1 = JSONManager.reparseJson(response.getData(), GameData.class);

        System.out.printf("Small Blind rzucił: %d", data1.getCurrentBet());
        afterSmallBlindTurn(response);
    }

    public static void afterSmallBlindTurn(ReceiveData receiveData) throws IOException {
        if (receiveData.getAction() == ActionType.BigBlindBetTurn){
            bigBlindTurn(receiveData);
        }
        else if(receiveData.getAction() == ActionType.NormalBetTurn){
            normalBets(receiveData);
        }
    }

    public static void bigBlindTurn(ReceiveData receiveData) {
        var data = JSONManager.reparseJson(receiveData.getData(), GameData.class);
    }

    public static void normalBets(ReceiveData receiveData) throws IOException {
        var data = JSONManager.reparseJson(receiveData.getData(), GameData.class);
        UIManager.showNormalBetTurn(data.isMyBet(), data.getMoney(), data.getCards());

        if(data.isMyBet()){
            myBet();
        }
        else{
            waitingForAnotherNormalBet();
        }
    }

    public static void myBetInNormalBets(){
        var response = myBet();

        // afterSmallBlindTurn(response);
    }

    public static void waitingForAnotherNormalBet() throws IOException {
        var response = SocketClientManager.i.getDataFromServer();

        var data1 = JSONManager.reparseJson(response.getData(), GameData.class);

        System.out.printf("Inny gracz rzucił: %d", data1.getCurrentBet());
        afterSmallBlindTurn(response);
    }
}
