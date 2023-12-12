package poker.client;

import poker.commons.MyLogger;

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

    public static void showGameIsStartedScreen(){
        MyLogger.logLineSep();
        MyLogger.logln("Gra się rozpoczęła");
        MyLogger.logLineSep();
    }
}
