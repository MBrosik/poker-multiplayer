package poker.client;

import poker.commons.MyLogger;
import poker.commons.game.elements.Card;
import poker.commons.socket.dataTypes.whileGame.BetInfo;
import poker.commons.socket.dataTypes.whileGame.StartGameDataInfo;
import poker.commons.socket.dataTypes.whileGame.PlayerType;

import java.util.ArrayList;

public class UIManager {
    public static void showHomeScreen() {
        MyLogger.logLineSep();
        MyLogger.logln("Wybierz opcje:");
        MyLogger.logln("1. Stwórz pokój ");
        MyLogger.logln("2. Dołącz do istniejącego pokoju ");
        MyLogger.logLineSep();
    }

    public static void showAddedRoomScreen(double code) {
        MyLogger.logLineSep();
        MyLogger.logln("Twój pokój został stworzony");
        MyLogger.logf("Numer pokoju: %d\n", (long) code);
        MyLogger.logln("Wybierz opcje:");
        MyLogger.logln("1. Oznacz, że jesteś gotowy do gry ");
        MyLogger.logLineSep();
    }

    public static void showAddedToRoomScreen() {
        MyLogger.logLineSep();
        MyLogger.logln("Zostałeś dodany do pokoju");
        MyLogger.logln("Wybierz opcje:");
        MyLogger.logln("1. Oznacz, że jesteś gotowy do gry ");
        MyLogger.logLineSep();
    }

    public static void showGameIsStartedScreen() {
        MyLogger.logLineSep();
        MyLogger.logln("Gra się rozpoczęła");
        MyLogger.logLineSep();
    }

    public static void showInfoAboutBlinds(StartGameDataInfo startGameDataInfo) {
        MyLogger.logLineSep();
        if (startGameDataInfo.getPlayerType() == PlayerType.SmallBlind) {
            MyLogger.logln("Zostałeś SmallBlind");
        } else if (startGameDataInfo.getPlayerType() == PlayerType.BigBlind) {
            MyLogger.logln("Zostałeś BigBlind");
        }

        MyLogger.logf("SmallBlind rzucił %d\n", startGameDataInfo.getSmallBlindBet());
        MyLogger.logf("BigBlind rzucił %d\n", startGameDataInfo.getBigBlindBet());
        MyLogger.logLineSep();
    }
    public static void showYourCards(ArrayList<Card> cards){
        MyLogger.logLineSep();
        MyLogger.logln("O to Twoje karty:");

        for (Card card : cards) {
            MyLogger.logln(card.toString());
        }
        MyLogger.logLineSep();
    }

//    public static void showSmallBlindTurnScreen(PlayerType type, int money) {
//        MyLogger.logLineSep();
//        MyLogger.logf("Twój stan konta: %d\n", money);
//        if (type == PlayerType.BigBlind) {
//            MyLogger.logln("Zostałeś wybrany jako BigBlind");
//            MyLogger.logln("Poczekaj aż SmallBlind poda stawkę");
//        } else if (type == PlayerType.SmallBlind) {
//            MyLogger.logln("Zostałeś wybrany jako SmallBlind");
//        } else {
//            MyLogger.logln("Zostałeś wybrany jako normalny gracz");
//            MyLogger.logln("Poczekaj aż SmallBlind poda stawkę");
//        }
//        MyLogger.logLineSep();
//    }

//    public static void showBigBlindTurnScreen(PlayerType type, int money) {
//        MyLogger.logLineSep();
//        MyLogger.logf("Twój stan konta: %d\n", money);
//        if (type == PlayerType.BigBlind) {
//            MyLogger.logln("Teraz Twoja kolej Big Blind");
//        } else {
//            MyLogger.logln("Teraz kolej Big blinda.");
//        }
//        MyLogger.logLineSep();
//    }

    public static void showBetTurn(BetInfo betInfo) {
        MyLogger.logLineSep();
        MyLogger.logf("Twój stan konta: %d\n", betInfo.getMoney());
        MyLogger.logf("Aktualna stawka na stole wynosi: %d\n", betInfo.getCurrentBet());

        if (betInfo.isMyBet()) {
            MyLogger.logln("Teraz Twoja kolej");
        } else {
            MyLogger.logln("Inny gracz teraz betuje");
        }

        MyLogger.logLineSep();
    }
}
