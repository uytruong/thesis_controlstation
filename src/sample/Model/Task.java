package sample.Model;

public class Task{
    public enum Status{
        NEW,
        READY,
        RUNNING,
        DONE
    }

    private int id, type;
    private int timeExecute, timeAppear, timeFinish = Integer.MAX_VALUE;
    private Point  goal;
    private Status status = Status.NEW;
    private int costOfRobotPath = Integer.MAX_VALUE;

    public Task(int type, int timeExecute, int timeAppear, Point goal) {
        this.type        = type;
        this.timeExecute = timeExecute;
        this.timeAppear  = timeAppear;
        this.goal        = goal.getPointClone();
    }

    /**
     * GETTER AND SETTER
     * */
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getType() {
        return type;
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
    public int getCostOfRobotPath() {
        return costOfRobotPath;
    }
    public void setCostOfRobotPath(int costOfRobotPath) {
        this.costOfRobotPath = costOfRobotPath;
    }
}
