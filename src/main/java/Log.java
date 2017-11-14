public class Log {
    private static boolean debug=false;
    private static boolean log=true;
    public static final String LINE = "--------------------";


    public static void log(String logString){
        if(log) {
            System.out.println(logString);
        }
    }

    public static void logDebug(String logString){
        if(debug) {
            System.out.println(logString);
        }
    }

    public static void setDebug(boolean debug) {
        Log.debug = debug;
    }

    public static void setLog(boolean log) {
        Log.log = log;
    }
    public static void logLine(){
        if(log){
            System.out.println("--------------------");
        }
    }
}
