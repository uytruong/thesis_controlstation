package sample.Model;

public class Task {
    public enum Status{
        NEW(0),
        READY(1),
        RUNNING(2),
        DONE(3);

        private final int value;

        Status(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public static Status getEnum(int value){ return Status.values()[value];}
    }


    private int id, type;
    private int timeExecute, timeAppear;
    private Point goal;
    private Status status;

    public Task(int type, int timeExecute, int timeAppear, Point goal) {
        this.type        = type;
        this.timeExecute = timeExecute;
        this.timeAppear  = timeAppear;
        this.goal        = goal;
        this.status      = Status.NEW;
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
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

}
