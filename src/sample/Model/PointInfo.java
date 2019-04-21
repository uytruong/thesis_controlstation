package sample.Model;

public class PointInfo {
    private int status  = Constant.PointStatus.NONE;
    private int robotId = -1;

    public PointInfo() {
    }

    public PointInfo(int status){
        this.status = status;
    }

    public PointInfo(PointInfo pointInfo){
        this.status  = pointInfo.getStatus();
        this.robotId = pointInfo.getRobotId();
    }

    public int getRobotId() {
        return robotId;
    }
    public void setRobotId(int robotId) {
        this.robotId = robotId;
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

}
