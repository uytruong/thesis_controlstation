package sample.Algorithm;

import sample.Manager.Context;
import sample.Manager.MapData;
import sample.Manager.RobotManager;
import sample.Manager.TaskManager;
import sample.Model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MultiPathPlanning {
    private TaskManager  taskManager;
    private RobotManager robotManager;
    private MapData      mapData;
    private int          timeAssignment;
    private Random       random;

    private int[]        optimalAssignment;
    private Conflict     conflict = new Conflict();

    public MultiPathPlanning(TaskManager taskManager, RobotManager robotManager, MapData mapData, int timeAssignment, Random random) {
        this.taskManager    = taskManager;
        this.robotManager   = robotManager;
        this.mapData        = mapData;
        this.timeAssignment = timeAssignment;
        this.random         = random;

    }

    private void assignTasks(){
        /*optimalAssignment returns an array of correspond taskId*/
        Assignment assignment = new Assignment(robotManager, taskManager,timeAssignment);
        assignment.execute();
    }

    private void simplePlan(){
        List<Robot> robotList = robotManager.getRobotList();

        /**
         * get simple plan for each robot
         * */
        for (Robot robot: robotList) {
            Task task = robot.getTask();
            if(task != null){
                SinglePathPlanning singlePathPlanning = new SinglePathPlanning(robot,mapData);
                if(singlePathPlanning.execute()){
                    robot.setMainPlanPointList(singlePathPlanning.getPlanPointList());
                }
            }
        }

        /**
         * write plan points into maps to get the conflict paths
         * then erase the plan paths which are conflicted
         * */
        for (Robot robot: robotList) {
            List<Point> mainPlanPointList = robot.getMainPlanPointList();
            int         timeOffset        = robot.getTimeFree();
            for (int idx = 0; idx < mainPlanPointList.size(); idx++) {
                Point point = mainPlanPointList.get(idx);
                int   x     = point.getX();
                int   y     = point.getY();
                if(mapData.getMapByTime(idx+timeOffset).getPointInfoByXY(x,y).getStatus()== PointInfo.Status.NONE) {
                    mapData.getMapByTime(idx+timeOffset).setPointInfoByPoint(point);
                }
                else{
                    Robot otherRobot = mapData.getMapByTime(idx+timeOffset).getPointInfoByXY(x,y).getRobot();
                    conflict.addRobot(otherRobot);
                    conflict.addRobot(robot);
                }
            }
        }

        for(Robot robot: robotList){
            if(robot.getTask() != null){
                if(conflict.existedRobot(robot)){
                    mapData.clearPointsFromMaps(robot.getMainPlanPointList(),robot.getTimeFree());
                }
                else{
                    robot.assignMainPlanPointList();
                    robot.setLastTimeUpdateToMap(robot.getLastTimeBusy());
                    robot.assignTask();
                }
            }

        }

    }
    private void priorPlan(){
        List<Robot> robotList = conflict.robotList;
        if(robotList.size()==0){
            System.out.println("There is no conflict among paths");
            return;
        }

        for (Robot robot: robotList) {
            System.out.println("conflict by robotId = "+robot.getId());
        }

        int totalCostMin = 0;

        for (Robot robot: robotList) {
            SinglePathPlanning singlePathPlanning = new SinglePathPlanning(robot,mapData);
            if(singlePathPlanning.execute()){
                totalCostMin += singlePathPlanning.getPathPlanningCost();
                robot.setMainPlanPointList(singlePathPlanning.getPlanPointList());
                mapData.writePointsToMaps(robot.getMainPlanPointList(),robot.getTimeFree());
            }
        }

        for (Robot robot: robotList) {
            mapData.clearPointsFromMaps(robot.getMainPlanPointList(),robot.getTimeFree());
        }


        int timeLoop = Config.getTimeLoopForPriorPlan(robotList.size());

        for (int i = 0; i <  timeLoop; i++) {
            Collections.shuffle(robotList,random);
            int totalCost = 0;
            for (Robot robot: robotList) {
                SinglePathPlanning singlePathPlanning = new SinglePathPlanning(robot,mapData);
                if(singlePathPlanning.execute()){
                    totalCost += singlePathPlanning.getPathPlanningCost();
                    robot.setSubPlanPointList(singlePathPlanning.getPlanPointList());
                    mapData.writePointsToMaps(robot.getSubPlanPointList(),robot.getTimeFree());
                }
            }
            for (Robot robot: robotList) {
                mapData.clearPointsFromMaps(robot.getSubPlanPointList(),robot.getTimeFree());
            }
            if(totalCost<totalCostMin){
                totalCostMin = totalCost;
                for (Robot robot: robotList) {
                    robot.setMainPlanPointList(robot.getSubPlanPointList());
                }
            }


        }

        for (Robot robot: robotList) {
            mapData.writePointsToMaps(robot.getMainPlanPointList(),robot.getTimeFree());
            robot.assignMainPlanPointList();
            robot.setLastTimeUpdateToMap(robot.getLastTimeBusy());
            robot.assignTask();
        }
    }

    public void futureCrashPlan(){
        System.out.println("futureCrashPlan");
        List<Robot> robotList = robotManager.getRobotList();
        Robot       lastRobot = robotList.get(robotList.size()-1);

        boolean     checkAll  = false;

        while (!checkAll){
            for (Robot robot: robotList) {
                Robot otherRobot = null;
                Point lastPoint = robot.getLastPoint();
                for (int time = robot.getTimeFree(); time < Context.timeMax; time++) {
                    PointInfo pointInfo = mapData.getMapByTime(time).getPointInfoByXY(lastPoint.getX(),lastPoint.getY());
                    if(pointInfo.getStatus() != PointInfo.Status.NONE){
                        otherRobot = pointInfo.getRobot();
                        break;
                    }
                }

                if(otherRobot != null){
                    Task task = robot.getTask();
                    Task otherTask = otherRobot.getTask();
                    mapData.clearPointsFromMaps(robot.getMainPlanPointList(),robot.getTimeFree());
                    mapData.clearPointsFromMaps(otherRobot.getMainPlanPointList(),otherRobot.getTimeFree());

                    robot.setTask(otherTask);
                    otherRobot.setTask(task);
                    if(robot.getTask() != null) {
                        SinglePathPlanning singlePathPlanning = new SinglePathPlanning(robot, mapData);
                        singlePathPlanning.execute();
                        robot.setMainPlanPointList(singlePathPlanning.getPlanPointList());

                        mapData.writePointsToMaps(robot.getMainPlanPointList(), robot.getTimeFree());
                        robot.assignTask();
                    }
                    if(otherRobot.getTask() != null) {
                        SinglePathPlanning singlePathPlanning = new SinglePathPlanning(otherRobot, mapData);
                        singlePathPlanning.execute();
                        otherRobot.setMainPlanPointList(singlePathPlanning.getPlanPointList());

                        mapData.writePointsToMaps(otherRobot.getMainPlanPointList(), otherRobot.getTimeFree());


                        otherRobot.assignTask();

                    }
                    break;
                }

                if (robot == lastRobot){
                    checkAll = true;
                }

            }

        }

    }


    public void execute(){
        assignTasks();
        simplePlan();
        //priorPlan();
        //futureCrashPlan();

    }










    public static class Config{
        public static int timeSolveMax = 3;


        public static int timeLoopForPriorPlanMax = 10;
        public static int getTimeLoopForPriorPlan(int size){
            int timeLoopMax = timeLoopForPriorPlanMax;
            int timeLoop = 1;
            for (int i = 2; i <= size; i++) {
                timeLoop *=i;
            }
            timeLoop = timeLoop/2;
            timeLoop = (timeLoopMax<timeLoop)?timeLoopMax: timeLoop;
            return timeLoop;
        }

        public static int getTimeSolveMaxMillis(){
            return (timeSolveMax*1000);
        }
    }



    private class Conflict{
        private List<Robot> robotList = new ArrayList<>();

        private void addRobot(Robot robot){
            if(!existedRobot(robot))
                robotList.add(robot);
        }
        private boolean existedRobot(Robot robot){
            for (Robot other: robotList) {
                if(robot == other)
                    return true;
            }
            return false;
        }
    }
}
