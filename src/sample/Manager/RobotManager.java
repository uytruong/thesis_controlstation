package sample.Manager;

import sample.Creator.RobotCreator;
import sample.Model.Robot;

import java.util.List;

public class RobotManager {
    private MapData     mapData;
    private List<Robot> robotList;

    public RobotManager(RobotCreator robotCreator, MapData mapData) {
        this.mapData    = mapData;
        this.robotList  = robotCreator.getRobotList();
    }

    public void updateByTime(int time){
        for (Robot robot: robotList) {
            /*update in case robot is not assigned new path */
            if (robot.getLastTimeBusy() <= time) {
                robot.updateByTime(time);
                for (int i = robot.getLastTimeUpdateToMap()+1; i <= time; i++) {
                    mapData.getMapByTime(i).setPointInfoByPoint(robot.getPointByTime(i));
                }
                robot.setLastTimeUpdateToMap(time);
            }
            /*update in case robot is assigned new path */
            else{
                for (int i = robot.getLastTimeUpdateToMap()+1; i <= robot.getLastTimeBusy(); i++) {
                    mapData.getMapByTime(i).setPointInfoByPoint(robot.getPointByTime(i));
                }
                robot.setLastTimeUpdateToMap(robot.getLastTimeBusy());
            }
        }
    }

    public void assignPlanPointListForAll(){
        for (Robot robot: robotList) {
            robot.assignMainPlanPointList();
            for (int time = robot.getLastTimeUpdateToMap()+1; time <= robot.getLastTimeBusy(); time++) {
                mapData.getMapByTime(time).setPointInfoByPoint(robot.getPointByTime(time));
            }
            robot.setLastTimeUpdateToMap(robot.getLastTimeBusy());
        }
    }


    public List<Robot> getRobotList() {
        return robotList;
    }
    public Robot       getRobotById(int id){ return robotList.get(id);}

}
