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
    public static String getStreamString(String msg){
        MyLogger.log(msg);
        return scanner.next();
    }
    public static int getStreamInt(){
        MyLogger.log("Wpisz: ");
        return scanner.nextInt();
    }

    public static int getStreamInt(String msg){
        MyLogger.log(msg);
//        scanner.nextLong();
        return scanner.nextInt();
    }
    public static long getStreamLong(){
        MyLogger.log("Wpisz: ");
        return scanner.nextLong();
    }

    public static long getStreamLong(String msg){
        MyLogger.log(msg);
        return scanner.nextLong();
    }
}
