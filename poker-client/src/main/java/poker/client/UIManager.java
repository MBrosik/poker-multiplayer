package poker.client;

import poker.commons.MyLogger;
import poker.commons.game.elements.Card;
import poker.commons.socket.dataTypes.whileGame.*;

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

    public static void showYourCards(ArrayList<Card> cards) {
        MyLogger.logLineSep();
        MyLogger.logln("O to Twoje karty:");

        for (Card card : cards) {
            MyLogger.logln(card.toString());
        }
        MyLogger.logLineSep();
    }

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

    public static void showNextRoundDealCards(NextRoundInfo data) {
        MyLogger.logLineSep();
        MyLogger.logln("Rozpoczęła się następna runda");
        MyLogger.logln("O to karty na stole:");

        for (Card card : data.getCardsOnTheTable()) {
            MyLogger.logln(card.toString());
        }

        MyLogger.logLineSep();
    }

    public static void showEndTurnScreen(EndGameInfo endGameInfo) {
        MyLogger.logLineSep();
        MyLogger.logln("Koniec rundy");
        MyLogger.logln("Karty na stole:");
        for (Card card : endGameInfo.getCardsOnTable()) {
            MyLogger.logln("\t" + card.toString());
        }
        MyLogger.logln("Twoje karty:");
        for (Card card : endGameInfo.getCardsInHand()) {
            MyLogger.logln("\t" + card.toString());
        }
        MyLogger.logln("Twój układ: " + endGameInfo.getVariation());
        if(endGameInfo.isWin()){
            MyLogger.logln("Wygrałeś!");
            MyLogger.logf("Zebrałeś: %d \n", endGameInfo.getHowMuchIWon());
        } else{
            MyLogger.logln("Niestety przegrałeś");
        }

        MyLogger.logf("Twój stan konta: %d \n", endGameInfo.getMyMoney());

    }
}
