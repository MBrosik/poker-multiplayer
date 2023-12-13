package poker.client;

import poker.commons.MyLogger;
import poker.commons.socket.dataTypes.whileGame.PlayerType;

public class UIManager {
    public static void showHomeScreen(){
        MyLogger.logLineSep();
        MyLogger.logln("Wybierz opcje:");
        MyLogger.logln("1. Stwórz pokój ");
        MyLogger.logln("2. Dołącz do istniejącego pokoju ");
        MyLogger.logLineSep();
    }

    public static void showAddedRoomScreen(double code){
        MyLogger.logLineSep();
        MyLogger.logln("Twój pokój został stworzony");
        MyLogger.logf("Numer pokoju: %d\n", (long) code);
        MyLogger.logln("Wybierz opcje:");
        MyLogger.logln("1. Oznacz, że jesteś gotowy do gry ");
        MyLogger.logLineSep();
    }

    public static void showAddedToRoomScreen(){
        MyLogger.logLineSep();
        MyLogger.logln("Zostałeś dodany do pokoju");
        MyLogger.logln("Wybierz opcje:");
        MyLogger.logln("1. Oznacz, że jesteś gotowy do gry ");
        MyLogger.logLineSep();
    }

    public static void showGameIsStartedScreen(PlayerType type, int money){
        MyLogger.logLineSep();
        MyLogger.logln("Gra się rozpoczęła");
        MyLogger.logf("Twój stan konta: %d\n", money);
        if(type == PlayerType.BigBlind){
            MyLogger.logln("Zostałeś wybrany jako BigBlind");
            MyLogger.logln("Poczekaj aż SmallBlind poda stawkę");
        } else if(type == PlayerType.SmallBlind){
            MyLogger.logln("Zostałeś wybrany jako SmallBlind");
        } else {
            MyLogger.logln("Zostałeś wybrany jako normalny gracz");
            MyLogger.logln("Poczekaj aż SmallBlind poda stawkę");
        }
        MyLogger.logLineSep();
    }
}
