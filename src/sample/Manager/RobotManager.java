package sample.Manager;

import sample.Creator.RobotCreator;
import sample.Model.Robot;

import java.util.List;

public class RobotManager {
    private MapManager  mapManager;
    private List<Robot> robotList;

    public RobotManager(RobotCreator robotCreator, MapManager mapManager) {
        this.mapManager = mapManager;
        robotList = robotCreator.getRobotList();

        init();
    }

    private void init(){
    }


    public void update(int timeUpdate){
        for (Robot robot: robotList) {
            if (robot.getLastTimeBusy() <= timeUpdate) {
                robot.update(timeUpdate);
                for (int i = robot.getLastTimeUpdateToMap()+1; i <= timeUpdate; i++) {
                    mapManager.getMapList().get(i).setStatus(robot.getPoint(i).getId(), robot.getPoint(i).getStatus());
                }
                robot.setLastTimeUpdateToMap(timeUpdate);
            }
            else{
                for (int i = robot.getLastTimeUpdateToMap()+1; i <= robot.getLastTimeBusy(); i++) {
                    mapManager.getMapList().get(i).setStatus(robot.getPoint(i).getId(),robot.getPoint(i).getStatus());
                }
                robot.setLastTimeUpdateToMap(robot.getLastTimeBusy());
            }
        }
    }

    public List<Robot> getRobotList() {
        return robotList;
    }


    public void printRobotListInfo(){
        for (Robot robot: robotList) {
            robot.printInfo();
        }
    }

}
