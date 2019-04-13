package sample.Creator;

import sample.Manager.MapManager;
import sample.Model.Constant;
import sample.Model.MapBase;
import sample.Model.Robot;

import java.util.ArrayList;
import java.util.List;

public class RobotCreator {
    private MapBase     mapBase;
    private List<Robot> robotList = new ArrayList<>();

    public RobotCreator(MapBaseCreator mapBaseCreator) {
         this.mapBase = mapBaseCreator.getMapBase();
    }

    public boolean createRobot(Robot robot){
        for (Robot otherRobot: robotList) {
            if(otherRobot.getPoint(0).getId() == robot.getPoint(0).getId()){ return false;}
        }
        if (mapBase.getStatus(robot.getPoint(0).getId()) == Constant.PointStatus.NONE){
            robot.setId(robotList.size());
            robotList.add(robot);
            return true;
        }
        else {
            return false;
        }
    }



    public List<Robot> getRobotList() {
        return robotList;
    }
    public void setRobotList(List<Robot> robotList) {
        this.robotList = robotList;
    }


}
