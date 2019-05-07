package sample.Model;

public class Task{
    public enum Status{
        NEW,
        READY,
        RUNNING,
        DONE
    }

    private int id;
    private final int   timeExecute;
    private final int   timeAppear;
    private final Point goal;

    private int    timeFinish = Integer.MAX_VALUE;
    private Status status     = Status.NEW;

    public Task(int timeExecute, int timeAppear, Point goal) {
        this.timeExecute = timeExecute;
        this.timeAppear  = timeAppear;
        this.goal        = goal;
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
}
