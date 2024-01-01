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

    private WhileGame(){}

    public static void start(ReceiveData receiveData) throws IOException {
        StartGameDataInfo data = JSONManager.reparseJson(receiveData.getData(), StartGameDataInfo.class);

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
            if(endGameInfo.getMyMoney() != 0) afterEndOfTurn();
        }
    }

    private static void placeBet(BetInfo data) {
        MyLogger.logLineSep();
        PlayerChoices command = PlayerChoices.Bet;
        int bet = -1;

        boolean stopWhileLoop = true;

        while (stopWhileLoop) {
            MyLogger.logln("Podaj stawkę, lub wpisz 'Pass', jeżeli chcesz spasować");
            MyLogger.logln("Runda betowania skończy się kiedy każdy gracz poda tę samą stawkę");
            var tempBet = MyScanner.getStreamString("Podaj: ");
            if (Objects.equals(tempBet, "Pass")) {
                MyLogger.logln("Pass");
                command = PlayerChoices.Pass;
                stopWhileLoop = false;
            }
            else {
                try {
                    bet = Integer.parseInt(tempBet);
                    if(data.getCurrentBet() <= bet && bet <= data.getMoney()) stopWhileLoop = false;
                    if(bet == data.getMoney()) {
                        MyLogger.logln("All in");
                        stopWhileLoop = false;
                    }
                    MyLogger.logln("Niewłaściwa stawka");

                } catch (NumberFormatException e) {
                    MyLogger.logln("Źle podana liczba");
                }

            }
        }

        sendBet(command, bet);
    }

    private static void sendBet(PlayerChoices command, int bet) {
        ReceiveData sendData;
        if (command == PlayerChoices.Bet) {
            sendData = new ReceiveData(ActionType.Bet, bet);
        }
        else {
            sendData = new ReceiveData(ActionType.Pass, null);
        }
        SocketClientManager.i.send(sendData, false);
    }

    public static void afterEndOfTurn() throws IOException {
        UIManager.showWaitForNewTurn();

        boolean stopWhileLoop = true;

        while(stopWhileLoop){
            String num = MyScanner.getStreamString();

            if(num.equals("1")){
                var data = new ReceiveData(ActionType.ReadyForNextRound, true);
                SocketClientManager.i.send(data, false);
                MyLogger.logln("Zostałeś oznaczony jako gotowy do gry. \nZa chwilę nowa runda się rozpocznie.");

                stopWhileLoop = false;
            } else if(num.equals("2")){
                var data = new ReceiveData(ActionType.ReadyForNextRound, false);
                SocketClientManager.i.send(data, false);
                MyLogger.logln("Wychodzisz z gry");
                stopWhileLoop = false;
            }
            else{
                MyLogger.logln("Zła opcja. Wybierz ponownie");
            }
        }

        var receivedData = SocketClientManager.i.getDataFromServer();

        if(receivedData.getAction() == ActionType.StartGameInfo){
            start(receivedData);
        } else{
            MyLogger.logln("Gra się zakończyła. Za mało graczy.");
        }
    }
}
