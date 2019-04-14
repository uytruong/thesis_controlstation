package sample.Manager;

import sample.Creator.RobotCreator;
import sample.Model.Robot;

import java.util.List;

public class RobotManager {

    private List<Robot>   robotList;

    public RobotManager(RobotCreator robotCreator) {
        robotList = robotCreator.getRobotList();
        init();
    }

    private void init(){
    }


    public List<Robot> getRobotList() {
        return robotList;
    }
    public Robot getRobot(int id){ return robotList.get(id);}


    public void printRobotListInfo(){
        for (Robot robot: robotList) {
            robot.printInfo();
        }
    }

}
