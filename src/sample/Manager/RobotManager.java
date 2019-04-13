package sample.Manager;

import sample.Creator.RobotCreator;
import sample.Model.Robot;

import java.util.ArrayList;
import java.util.List;

public class RobotManager {

    private List<Robot>   robotList;

    public RobotManager(RobotCreator robotCreator) {
        robotList = new ArrayList<>(robotCreator.getRobotList());
        init();
    }

    private void init(){
    }

    public void update(){
        for (Robot robot: robotList) {
                robot.update(Context.time);
        }
    }



    public List<Robot> getRobotList() {
        return robotList;
    }
    public void setRobotList(List<Robot> robotList) {
        this.robotList = robotList;
    }
    public Robot getRobot(int id){ return robotList.get(id);}

}
