package sample.Manager;

import sample.Creator.RobotCreator;
import sample.Model.Point;
import sample.Model.Robot;

import java.util.ArrayList;
import java.util.List;

public class RobotManager {
    private MapData     mapData;
    private List<Robot> robotList;

    private int         rotateCost;
    private int         stepCost;

    public RobotManager(RobotCreator robotCreator, MapData mapData) {
        this.mapData    = mapData;
        this.robotList  = robotCreator.getRobotList();
    }

    public void updateByTime(int time){
        List<Point> robotPointListThisTime = new ArrayList<>();
        for (Robot robot: robotList) {
            if (robot.getLastTimeBusy() <= time) {
                robot.updateByTime(time);
                for (int i = robot.getLastTimeUpdateToMap()+1; i <= time; i++) {
                    mapData.getMapByTime(i).setPointInfoByPoint(robot.getPointByTime(i));
                }
                robot.setLastTimeUpdateToMap(time);
            }
            else{
                for (int i = robot.getLastTimeUpdateToMap()+1; i <= robot.getLastTimeBusy(); i++) {
                    mapData.getMapByTime(i).setPointInfoByPoint(robot.getPointByTime(i));
                }
                robot.setLastTimeUpdateToMap(robot.getLastTimeBusy());
            }

            Point robotPointThisTime = robot.getPointByTime(time);
            robotPointListThisTime.add(robotPointThisTime);
            for (Point point: robotPointListThisTime) {
                if(point.getRobot() != robotPointThisTime.getRobot()){
                    if(Point.isCoincident(point,robotPointThisTime)){
                        Context.logData("Crash by robot Manager Checking:");
                        Context.logData("   --- robot: " + point.getRobot().getId() + " & " + robotPointThisTime.getRobot().getId() );
                    }
                }
            }
        }
    }

    public void updatePathCost(int time){
         for (Robot robot: robotList) {
            Point point    = robot.getPointByTime(time);
            Point prePoint = robot.getPointByTime(time-1);
            if (!Point.isCoincident(point,prePoint))
                stepCost++;
            else if (!Point.isHeadingSameDirection(point,prePoint))
                rotateCost++;
        }
    }

    public List<Robot> getRobotList() {
        return robotList;
    }
    public List<Robot> getFreeRobotList(){
        List<Robot> freeRobotList = new ArrayList<>();
        for (Robot robot: this.robotList) {
            if(robot.getStatus() == Robot.Status.FREE)
                freeRobotList.add(robot);
        }
        return freeRobotList;
    }
    public List<Robot> getFreeRobotListWithTask(){
        List<Robot> freeRobotListWithTask = new ArrayList<>();
        for (Robot robot: this.robotList) {
            if((robot.getStatus() == Robot.Status.FREE) & (robot.getTask() !=null))
                freeRobotListWithTask.add(robot);
        }
        return freeRobotListWithTask;
    }
    public List<Robot> getFreeRobotListWithTaskButNoPlan(){
        List<Robot> freeRobotListWithTaskButNoPlan = new ArrayList<>();
        for (Robot robot: this.robotList) {
            if((robot.getStatus() == Robot.Status.FREE) & (robot.getTask() !=null) & (!robot.isMainPlanSuccess()))
                freeRobotListWithTaskButNoPlan.add(robot);
        }
        return freeRobotListWithTaskButNoPlan;
    }
    public List<Robot> getBusyAndFreeWithoutTaskRobotList(){
        List<Robot> busyAndFreeWithoutTaskRobotList = new ArrayList<>();
        for (Robot robot: this.robotList) {
            if((robot.getStatus() == Robot.Status.FREE) & (robot.getTask() ==null)
                    | (robot.getStatus() == Robot.Status.BUSY))
                busyAndFreeWithoutTaskRobotList.add(robot);
        }
        return busyAndFreeWithoutTaskRobotList;
    }
    public List<Robot> getBusyRobotList(){
        List<Robot> robotList = new ArrayList<>();
        for (Robot robot: this.robotList) {
            if(robot.getStatus() == Robot.Status.BUSY)
                robotList.add(robot);
        }
        return robotList;
    }

    public int getRotateCost() {
        return rotateCost;
    }
    public int getStepCost() {
        return stepCost;
    }

}
