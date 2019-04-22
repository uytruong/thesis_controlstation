package sample.Model;

public class PointInfo {
    public enum Status{
        NONE(0),
        SHELF(1),
        ROBOT_UP(2),
        ROBOT_DOWN(3),
        ROBOT_LEFT(4),
        ROBOT_RIGHT(5);

        private final int value;

        Status(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public static Status getEnum(int value){ return Status.values()[value];}
    }

    private Status status  = Status.NONE;

    public PointInfo() {
    }

    public PointInfo(Status status){
        this.status = status;
    }

    public PointInfo(PointInfo pointInfo){
        this.status  = pointInfo.getStatus();
    }


    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

}
