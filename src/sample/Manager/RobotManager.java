package sample.Manager;

import sample.Creator.RobotCreator;
import sample.Model.Robot;

import java.util.ArrayList;
import java.util.List;

public class RobotManager {
    private RobotCreator robotCreator;

    private List<Robot>   robotList;
    private List<Integer> queue;

    public RobotManager(RobotCreator robotCreator) {
        this.robotCreator = robotCreator;
        init();
    }

    private void init(){
        robotList = new ArrayList<>(robotCreator.getRobotList());
    }

    public void update(){
        for (Robot robot: robotList) {
                robot.update(Context.time);
        }
    }



    public RobotCreator getRobotCreator() {
        return robotCreator;
    }
    public void setRobotCreator(RobotCreator robotCreator) {
        this.robotCreator = robotCreator;
    }
    public List<Robot> getRobotList() {
        return robotList;
    }
    public void setRobotList(List<Robot> robotList) {
        this.robotList = robotList;
    }
    public Robot getRobot(int id){ return robotList.get(id);}

    public List<Integer> getQueue() {
        return queue;
    }
    public void setQueue(List<Integer> queue) {
        this.queue = queue;
    }

}
