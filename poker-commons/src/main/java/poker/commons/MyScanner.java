package poker.commons;

import java.io.InputStream;
import java.util.Scanner;

public class MyScanner {
    private static Scanner scanner = new Scanner(System.in);

    public static void changeInput(InputStream io){
        scanner = new Scanner(io);
    }

    public static String getStreamString(){
        MyLogger.log("Wpisz: ");
        return scanner.next();
    }
    public static int getStreamInt(){
        MyLogger.log("Wpisz: ");
        return scanner.nextInt();
    }
}
