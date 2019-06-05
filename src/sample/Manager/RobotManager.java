package sample.Manager;

import sample.Creator.RobotCreator;
import sample.Model.Point;
import sample.Model.Robot;
import sample.Model.Task;

import java.util.ArrayList;
import java.util.List;

import static sample.Model.Robot.Status.BUSY;
import static sample.Model.Robot.Status.FULL;

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
        List<Robot> robotList = new ArrayList<>();
        for (Robot robot: this.robotList) {
            if(robot.getStatus() == Robot.Status.FREE)
                robotList.add(robot);
        }
        return robotList;
    }

    public List<Robot> getRobotListWithFullReportOrFreeTask(){
        List<Robot> robotList = new ArrayList<>();
        for (Robot robot: getRobotListWithTask()) {
            if((robot.getStatus() == Robot.Status.FREE) |
              ((robot.getStatus() == Robot.Status.FULL) & (robot.getTask().getStatus() == Task.Status.REPORTING)))
                robotList.add(robot);
        }
        return robotList;
    }

    public List<Robot> getRobotListWithTask(){
        List<Robot> robotList = new ArrayList<>();
        for (Robot robot: this.robotList) {
            if(robot.getTask() != null)
                robotList.add(robot);
        }
        return robotList;
    }

    public List<Robot> getRobotListWithoutPlan(List<Robot> robotList){
        List<Robot> robotListWithoutPlan = new ArrayList<>();
        for (Robot robot: robotList) {
            if(!robot.isMainPlanSuccess())
                robotListWithoutPlan.add(robot);
        }
        return robotListWithoutPlan;
    }

    public List<Robot> getOtherRobotList(Robot robot){
        List<Robot> robotList = new ArrayList<>();
        for (Robot other: this.robotList) {
            if(robot != other)
                robotList.add(other);
        }
        return robotList;
    }

    public List<Robot> getOtherRobotList(List<Robot> robotList){
        List<Robot> otherList = new ArrayList<>();
        for (Robot robot: this.robotList) {
            boolean valid = true;
            for (Robot r: robotList) {
                if(robot == r){
                    valid = false;
                    break;
                }
            }
            if(valid)
                otherList.add(robot);
        }
        return otherList;
    }

    public List<Robot> getOtherRobotListExceptRobot(List<Robot> robotList, Robot robot) {
        List<Robot> otherList = getOtherRobotList(robotList);
        List<Robot> otherListExceptRobot = new ArrayList<>();
        for (Robot r: otherList) {
            if(r != robot)
                otherListExceptRobot.add(r);
        }
        return otherListExceptRobot;
    }

    public List<Robot> getKnownRobotList(){
        List<Robot> robotList = new ArrayList<>();
        for (Robot robot: this.robotList) {
            if((robot.getStatus() == BUSY) | ((robot.getStatus() == Robot.Status.FREE) & (robot.getTask() == null)))
                robotList.add(robot);
            if (robot.getStatus() == FULL){
                if(robot.getTask().getStatus() == Task.Status.RETURNING)
                    robotList.add(robot);
            }
        }
        return robotList;
    }

    public List<Robot> removeRobotFromRobotList(List<Robot> robotList, Robot robot){
        List<Robot> robotListForPlan = new ArrayList<>();
        for (Robot other: robotList) {
            if(robot != other)
                robotListForPlan.add(other);
        }
        return robotListForPlan;
    }

    public int getRotateCost() {
        return rotateCost;
    }
    public int getStepCost() {
        return stepCost;
    }

}
