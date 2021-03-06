package sample.Manager;

public class Context {


    public static int time = 0;
    public static int timeMax = 200;

    public static int timeSolveMaxMillis = 0;
    public static long timeStartThreadMillis;
    public static long timeEndThreadMillis;
    public static long timeRunThreadMillis;

    public static int stepCost = 0;
    public static int rotateCost = 0;

    public static boolean simulating = false;
    public static boolean solvingMultiPath = false;

    public static int playSpeed = 1;


    public static void reset(){
        time = 0;
        stepCost = 0;
        rotateCost = 0;
        simulating = false;
        solvingMultiPath = false;
    }

    public static int getTimelineDurationMillis(){
        return (1000/playSpeed);
    }


    public static void logData(String data){
        System.out.println(data);
    }
}
