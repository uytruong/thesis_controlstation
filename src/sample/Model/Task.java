package sample.Model;

public class Task{
    public enum Status{
        NEW,
        READY,
        BOUND,
        RUNNING,
        REPORTING,
        RETURNING,
        DONE
    }

    private int   id;
    private int   timeExecute;
    private int   timeAppear;
    private Point goal;
    private Point station;

    private int    timeReturn = 2;
    private int    timeArrived= Integer.MAX_VALUE;
    private int    timeFinish = Integer.MAX_VALUE;
    private Status status     = Status.NEW;
    private Robot  robot      = null;

    public Task(int timeExecute, int timeAppear, Point goal, Point station) {
        this.timeExecute = timeExecute;
        this.timeAppear  = timeAppear;
        this.goal        = goal;
        this.station     = station;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getTimeExecute() {
        return timeExecute;
    }
    public int getTimeAppear() {
        return timeAppear;
    }
    public Point getGoal() {
        return goal;
    }

    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public int getTimeFinish() {
        return timeFinish;
    }
    public void setTimeFinish(int timeFinish) {
        this.timeFinish = timeFinish;
    }
    public Robot getRobot() {
        return robot;
    }
    public void setRobot(Robot robot) {
        this.robot = robot;
    }
    public int getTimeArrived() {
        return timeArrived;
    }
    public void setTimeArrived(int timeArrived) {
        this.timeArrived = timeArrived;
    }
    public Point getStation() {
        return station;
    }
    public void setGoal(Point goal) {
        this.goal = goal;
    }
    public int getTimeReturn() {
        return timeReturn;
    }
    public void setTimeReturn(int timeReturn) {
        this.timeReturn = timeReturn;
    }
}
