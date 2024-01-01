package poker.commons;

import java.io.InputStream;
import java.util.Scanner;

public class MyScanner {
    private static final String INPUT_STRING = "Wpisz: ";
    private MyScanner(){}

    private static Scanner scanner = new Scanner(System.in);

    public static void changeInput(InputStream io){
        scanner = new Scanner(io);
    }

    public static String getStreamString(){
        MyLogger.log(INPUT_STRING);
        return scanner.next();
    }
    public static String getStreamString(String msg){
        MyLogger.log(msg);
        return scanner.next();
    }
    public static int getStreamInt(){
        MyLogger.log(INPUT_STRING);
        return scanner.nextInt();
    }

    public static int getStreamInt(String msg){
        MyLogger.log(msg);
        return scanner.nextInt();
    }
    public static long getStreamLong(){
        MyLogger.log(INPUT_STRING);
        return scanner.nextLong();
    }

    public static long getStreamLong(String msg){
        MyLogger.log(msg);
        return scanner.nextLong();
    }
}
