package sample.Model;

public class Task {
    private int id, type;
    private int timeExecute, timeAppear, timeValid;
    private Point goal;
    private int status;

    public Task(int type, int timeExecute, int timeAppear, Point goal) {
        this.type        = type;
        this.timeExecute = timeExecute;
        this.timeAppear  = timeAppear;
        this.goal        = goal;
        init();
    }

    private void init(){
        this.status    = Constant.TaskStatus.NEW;
        this.timeValid = this.timeAppear;
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
    public int getTimeValid() {
        return timeValid;
    }
    public void setTimeValid(int timeValid) {
        this.timeValid = timeValid;
    }

    public void printInfo(){
        String info = "Task Info: id = " + id + "; type = " + type + "; goal at: id = " + goal.getId();
        System.out.println(info);
    }
}
