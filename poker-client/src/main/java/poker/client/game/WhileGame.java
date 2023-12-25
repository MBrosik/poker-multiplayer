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
            myBetAsSmallBlind(data);
        } else {
            waitingForSmallBlindBet();
        }
    }

    private static ReceiveData myBet(GameData gameData, boolean addCheckPassStatus) {
        MyLogger.logLineSep();
        int bet;
        while(true){
            bet = MyScanner.getStreamInt("Podaj stawkę:");
            if(bet >= gameData.getCurrentBet() && bet <= gameData.getMoney()){
                break;
            }
            MyLogger.logln("Niewłaściwa stawka");
        }

        if(addCheckPassStatus && bet == gameData.getCurrentBet()){
            MyLogger.logLineSep();
            MyLogger.logln("Check");
            MyLogger.logLineSep();
        }
        else if(addCheckPassStatus && bet == 0){
            MyLogger.logLineSep();
            MyLogger.logln("Pass");
            MyLogger.logLineSep();
        }

        var data = new ReceiveData(ActionType.Bet, bet);
        return SocketClientManager.i.send(data, true);
    }

    public static void myBetAsSmallBlind(GameData data) throws IOException {
        var response = myBet(data, false);

        afterSmallBlindTurn(response);
    }

    public static void waitingForSmallBlindBet() throws IOException {
        var response = SocketClientManager.i.getDataFromServer();

        var data1 = JSONManager.reparseJson(response.getData(), GameData.class);

        System.out.printf("Small Blind rzucił: %d\n", data1.getCurrentBet());
        afterSmallBlindTurn(response);
    }

    public static void afterSmallBlindTurn(ReceiveData receiveData) throws IOException {
        if (receiveData.getAction() == ActionType.BigBlindBetTurn){
            bigBlindTurn(receiveData);
        }
        else if(receiveData.getAction() == ActionType.NormalBetTurn){
            normalBets(receiveData, true);
        }
    }

    public static void bigBlindTurn(ReceiveData receiveData) {
        var data = JSONManager.reparseJson(receiveData.getData(), GameData.class);
    }

    public static void normalBets(ReceiveData receiveData, boolean showCards) throws IOException {
        var data = JSONManager.reparseJson(receiveData.getData(), GameData.class);
        UIManager.showNormalBetTurn(data.isMyBet(), data.getMoney(), data.getCards(), showCards);
        GameData gameData = JSONManager.reparseJson(receiveData.getData(), GameData.class);

        if(data.isMyBet()){
            myBetInNormalBets(gameData);
        } else{
            waitingForAnotherNormalBet();
        }
    }

    public static void myBetInNormalBets(GameData gameData) throws IOException {
        var response = myBet(gameData, true);

         afterNormalBet(response);
    }

    public static void waitingForAnotherNormalBet() throws IOException {
        var response = SocketClientManager.i.getDataFromServer();

        var data1 = JSONManager.reparseJson(response.getData(), GameData.class);

        System.out.printf("Inny gracz rzucił: %d\n", data1.getCurrentBet());
        afterNormalBet(response);
    }

    public static void afterNormalBet(ReceiveData data) throws IOException {
        if(data.getAction() == ActionType.NormalBetTurn){
            normalBets(data, false);
        }
        else{
            // next round flop actually
        }
    }
}
