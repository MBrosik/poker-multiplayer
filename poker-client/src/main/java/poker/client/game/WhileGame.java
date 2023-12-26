package poker.client.game;

import poker.client.SocketClientManager;
import poker.client.UIManager;
import poker.client.game.whileGame.PlayerChoices;
import poker.commons.JSONManager;
import poker.commons.MyLogger;
import poker.commons.MyScanner;
import poker.commons.socket.ReceiveData;
import poker.commons.socket.dataTypes.ActionType;
import poker.commons.socket.dataTypes.whileGame.BetInfo;
import poker.commons.socket.dataTypes.whileGame.StartGameDataInfo;

import java.io.IOException;
import java.util.Objects;

public class WhileGame {
    public static void start(ReceiveData receiveData) throws IOException {
        StartGameDataInfo data = JSONManager.reparseJson(receiveData.getData(), StartGameDataInfo.class);
        UIManager.showGameIsStartedScreen();

        if (receiveData.getAction() == ActionType.StartGameInfo) {
            UIManager.showInfoAboutBlinds(data);
            UIManager.showYourCards(data.getCards());
            waitForBetInfo();
        }
    }

    public static void waitForBetInfo() throws IOException {
        var receiveData = SocketClientManager.i.getDataFromServer();
        if (receiveData.getAction() != ActionType.Bet) return;
        BetInfo data = JSONManager.reparseJson(receiveData.getData(), BetInfo.class);
        UIManager.showBetTurn(data);

        if (data.isMyBet()) {
            placeBet(data);

            waitForBetInfo();
        } else {
            waitForBetInfo();
        }
    }

    private static void placeBet(BetInfo data) {
        MyLogger.logLineSep();
        PlayerChoices command = PlayerChoices.Bet;
        int bet = -1;

        while (true) {
            MyLogger.logln("Podaj stawkę, lub wpisz 'Pass', jeżeli chcesz spasować");
            var tempBet = MyScanner.getStreamString("Podaj:");
            if (Objects.equals(tempBet, "Pass")) {
                MyLogger.logln("Pass");
                command = PlayerChoices.Pass;
                break;
            }
//            else if (Objects.equals(tempBet, "check")) {
//                MyLogger.logln("check");
//                command = PlayerChoices.Check;
//                break;
//            }
            else {
                try {
                    bet = Integer.parseInt(tempBet);
                    if(data.getCurrentBet() <= bet && bet <= data.getMoney()) break;
                    if(bet == data.getMoney()) {
                        MyLogger.logln("All in");
                        break;
                    }
                    MyLogger.logln("Niewłaściwa stawka");

                } catch (NumberFormatException e) {
                    MyLogger.logln("Źle podana liczba");
                }

            }
        }

        ReceiveData sendData;
        if (command == PlayerChoices.Bet) {
            sendData = new ReceiveData(ActionType.Bet, bet);
        }
//        else if (command == PlayerChoices.Check) {
//            sendData = new ReceiveData(ActionType.Check, null);
//        }
        else {
            sendData = new ReceiveData(ActionType.Pass, null);
        }
        SocketClientManager.i.send(sendData, false);
    }


//    private static ReceiveData myBet(GameData gameData, boolean addCheckPassStatus) {
//        MyLogger.logLineSep();
//        int bet;
//        while(true){
//            bet = MyScanner.getStreamInt("Podaj stawkę:");
//            if(bet >= gameData.getCurrentBet() && bet <= gameData.getMoney()){
//                break;
//            }
//            MyLogger.logln("Niewłaściwa stawka");
//        }
//
//        if(addCheckPassStatus && bet == gameData.getCurrentBet()){
//            MyLogger.logLineSep();
//            MyLogger.logln("Check");
//            MyLogger.logLineSep();
//        }
//        else if(addCheckPassStatus && bet == 0){
//            MyLogger.logLineSep();
//            MyLogger.logln("Pass");
//            MyLogger.logLineSep();
//        }
//
//        var data = new ReceiveData(ActionType.Bet, bet);
//        return SocketClientManager.i.send(data, true);
//    }
//
//    public static void myBetAsSmallBlind(GameData data) throws IOException {
//        var response = myBet(data, false);
//
//        afterSmallBlindTurn(response);
//    }
//
//    public static void waitingForSmallBlindBet() throws IOException {
//        var response = SocketClientManager.i.getDataFromServer();
//
//        var data1 = JSONManager.reparseJson(response.getData(), GameData.class);
//
//        System.out.printf("Small Blind rzucił: %d\n", data1.getCurrentBet());
//        afterSmallBlindTurn(response);
//    }
//
//    public static void afterSmallBlindTurn(ReceiveData receiveData) throws IOException {
//        if (receiveData.getAction() == ActionType.BigBlindBetTurn){
//            bigBlindTurn(receiveData);
//        }
//        else if(receiveData.getAction() == ActionType.NormalBetTurn){
//            normalBets(receiveData, true);
//        }
//    }
//
//    public static void bigBlindTurn(ReceiveData receiveData) {
//        var data = JSONManager.reparseJson(receiveData.getData(), GameData.class);
//    }
//
//    public static void normalBets(ReceiveData receiveData, boolean showCards) throws IOException {
//        var data = JSONManager.reparseJson(receiveData.getData(), GameData.class);
//        UIManager.showNormalBetTurn(data.isMyBet(), data.getMoney(), data.getCards(), showCards);
//        GameData gameData = JSONManager.reparseJson(receiveData.getData(), GameData.class);
//
//        if(data.isMyBet()){
//            myBetInNormalBets(gameData);
//        } else{
//            waitingForAnotherNormalBet();
//        }
//    }
//
//    public static void myBetInNormalBets(GameData gameData) throws IOException {
//        var response = myBet(gameData, true);
//
//         afterNormalBet(response);
//    }
//
//    public static void waitingForAnotherNormalBet() throws IOException {
//        var response = SocketClientManager.i.getDataFromServer();
//
//        var data1 = JSONManager.reparseJson(response.getData(), GameData.class);
//
//        System.out.printf("Inny gracz rzucił: %d\n", data1.getCurrentBet());
//        afterNormalBet(response);
//    }
//
//    public static void afterNormalBet(ReceiveData data) throws IOException {
//        if(data.getAction() == ActionType.NormalBetTurn){
//            normalBets(data, false);
//        }
//        else{
//            // next round flop actually
//        }
//    }
}
