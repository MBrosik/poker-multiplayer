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
}
