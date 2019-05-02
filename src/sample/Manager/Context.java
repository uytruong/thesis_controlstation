package sample.Manager;

public class Context {
    public static int time = 0;
    public static int timeMax = 200;

    public static int totalCost = 0;

    public static boolean simulating = false;
    public static boolean solvingMultiPath = false;

    public static int increaseTime(){
        System.out.println("@CurrentTime = " + (++time) +"+");
        return time;
    }

}
