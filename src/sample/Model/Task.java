package sample.Model;

public class Task {
    private int id, type;
    private int timeExecute, timeAppear;
    private Point goal;
    private int status;

    public Task(int type, int timeExecute, int timeAppear, Point goal) {
        this.type        = type;
        this.timeExecute = timeExecute;
        this.timeAppear  = timeAppear;
        this.goal        = goal;
        this.status      = Constant.TaskStatus.NEW;
    }


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public int getTimeExecute() {
        return timeExecute;
    }
    public void setTimeExecute(int timeExecute) {
        this.timeExecute = timeExecute;
    }
    public int getTimeAppear() {
        return timeAppear;
    }
    public void setTimeAppear(int timeAppear) {
        this.timeAppear = timeAppear;
    }
    public Point getGoal() {
        return goal;
    }
    public void setGoal(Point goal) {
        this.goal = goal;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

}
