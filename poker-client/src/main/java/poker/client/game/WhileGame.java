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
import poker.commons.socket.dataTypes.whileGame.EndGameInfo;
import poker.commons.socket.dataTypes.whileGame.NextRoundInfo;
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
        if (receiveData.getAction() == ActionType.Bet) {
            BetInfo data = JSONManager.reparseJson(receiveData.getData(), BetInfo.class);
            UIManager.showBetTurn(data);

            if (data.isMyBet()) {
                placeBet(data);

                waitForBetInfo();
            } else {
                waitForBetInfo();
            }
        } else if(receiveData.getAction() == ActionType.NextRound){
            NextRoundInfo data = JSONManager.reparseJson(receiveData.getData(), NextRoundInfo.class);
            UIManager.showNextRoundDealCards(data);
            waitForBetInfo();
        }
        else if(receiveData.getAction() == ActionType.EndTurn){
            EndGameInfo endGameInfo = JSONManager.reparseJson(receiveData.getData(), EndGameInfo.class);

            UIManager.showEndTurnScreen(endGameInfo);
//            MyLogger.logln(endGameInfo.isWin());
        }
    }

    private static void placeBet(BetInfo data) {
        MyLogger.logLineSep();
        PlayerChoices command = PlayerChoices.Bet;
        int bet = -1;

        while (true) {
            MyLogger.logln("Podaj stawkę, lub wpisz 'Pass', jeżeli chcesz spasować");
            var tempBet = MyScanner.getStreamString("Podaj: ");
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
}
