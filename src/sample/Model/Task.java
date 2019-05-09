package sample.Model;

public class Task{
    public enum Status{
        NEW,
        READY,
        RUNNING,
        DONE
    }

    private int   id;
    private int   timeExecute;
    private int   timeAppear;
    private Point goal;

    private int    timeFinish = Integer.MAX_VALUE;
    private Status status     = Status.NEW;
    private Robot  robot      = null;

    public Task(int timeExecute, int timeAppear, Point goal) {
        this.timeExecute = timeExecute;
        this.timeAppear  = timeAppear;
        this.goal        = goal;
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
}
