package sample.Model;

import java.util.ArrayList;
import java.util.List;

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

        public String getString(){
            switch (value){
                case 0:  return "NONE";
                case 1:  return "SHELF";
                case 2:  return "ROBOT_UP";
                case 3:  return "ROBOT_DOWN";
                case 4:  return "ROBOT_LEFT";
                default: return "ROBOT_RIGHT";
            }
        }

        public static List<String> getRobotHeadingStringList(){
            List<String> list = new ArrayList<>();
            for (int i = 2; i < 6; i++) {
                list.add(getEnum(i).getString());
            }
            return list;
        }

        public static Status getEnum(int value){ return Status.values()[value];}

    }

    private Status status  = Status.NONE;
    private Robot  robot   = null;
    public PointInfo() {
    }

    public PointInfo(Status status){
        this.status = status;
    }

    public PointInfo(PointInfo pointInfo){
        this.status  = pointInfo.getStatus();
        this.robot   = pointInfo.getRobot();
    }

    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

    public Robot getRobot() {
        return robot;
    }
    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    public boolean isEmpty(){ return (status == Status.NONE);}
    public boolean isShelf(){ return (status == Status.SHELF);}
}
