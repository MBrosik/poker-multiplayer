package poker.commons;

public class MyLogger {
    private static final String LINE_SEP = "==========================================================================";

    private MyLogger(){}
    public static void log(String msg){
        System.out.print(msg);
    }
    public static void logln(String msg){
        System.out.println(msg);
    }
    public static void logf(String format, Object ... args){
        System.out.printf(format, args);
    }

    public static void logLineSep(){
        System.out.println(LINE_SEP);
    }

    public static void elog(String msg){
        System.err.print(msg);
    }
    public static void elogln(String msg){
        System.err.println(msg);
    }
    public static void elogf(String format, Object ... args){
        System.err.printf(format, args);
    }

    public static void elogLineSep(){
        System.err.println(LINE_SEP);
    }
}
