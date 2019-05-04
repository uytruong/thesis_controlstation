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

    private Conflict     conflict = new Conflict();

    public MultiPathPlanning(TaskManager taskManager, RobotManager robotManager, MapData mapData, int timeAssignment, Random random) {
        this.taskManager    = taskManager;
        this.robotManager   = robotManager;
        this.mapData        = mapData;
        this.timeAssignment = timeAssignment;
        this.random         = random;
    }

    private void clearOldPlan(){
        for (Robot robot: robotManager.getRobotList()) {
            robot.setTask(null);
            robot.setMainPlanPointList(new ArrayList<>());
            robot.setSubPlanPointList(new ArrayList<>());
        }
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
            if(robot.getTask() != null){
                SinglePathPlanning singlePathPlanning = new SinglePathPlanning(robot,mapData);
                if(singlePathPlanning.execute()){
                    robot.setMainPlanPointList(singlePathPlanning.getPlanPointList());
                }
                else{
                    robot.setTask(null);
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
                Map   map   = mapData.getMapByTime(idx+timeOffset);
                if(map.getPointInfoByXY(x,y).getStatus()== PointInfo.Status.NONE) {
                    map.setPointInfoByPoint(point);
                }
                else{
                    Robot sRobot = map.getPointInfoByXY(x,y).getRobot();
                    conflict.add(sRobot);
                    conflict.add(robot);
                }
            }
        }

        for (Robot robot: robotManager.getRobotList()){
            if(conflict.exist(robot) & (robot.getTask() != null)){
                mapData.clearPointsFromMaps(robot.getMainPlanPointList(),robot.getTimeFree());
                robot.getMainPlanPointList().clear();
            }
            else {
                robot.assignTaskByPlan();
            }
        }

    }
    private void priorPlan(){
        List<Robot> conflictRobots = conflict.robotList;

        if(conflictRobots.size()==0){
            System.out.println("There is no conflict among paths");
            return;
        }

        for (Robot robot: conflictRobots) {
            System.out.println("conflict by robotId = "+robot.getId());
        }

        int totalCostMin    = 0;
        int taskCpltNumMax  = 0;

        for (Robot robot: conflictRobots) {
            if(robot.getTask()!= null){
                SinglePathPlanning singlePathPlanning = new SinglePathPlanning(robot,mapData);
                if(singlePathPlanning.execute()){
                    totalCostMin   += singlePathPlanning.getPathPlanningCost();
                    taskCpltNumMax ++;

                    robot.setMainPlanPointList(singlePathPlanning.getPlanPointList());
                    mapData.writePointsToMaps(robot.getMainPlanPointList(),robot.getTimeFree());
                }
            }
        }

        for (Robot robot: conflictRobots) {
            if(robot.getTask() != null)
                mapData.clearPointsFromMaps(robot.getMainPlanPointList(),robot.getTimeFree());
        }

        int timeLoop = Config.getTimeLoopForPriorPlan(conflictRobots.size());

        for (int i = 0; i <  timeLoop; i++) {
            Collections.shuffle(conflictRobots,random);
            int totalCost   = 0;
            int taskCpltNum = 0;
            for (Robot robot: conflictRobots) {
                if(robot.getTask()!=null){
                    SinglePathPlanning singlePathPlanning = new SinglePathPlanning(robot,mapData);
                    if(singlePathPlanning.execute()){
                        totalCost += singlePathPlanning.getPathPlanningCost();
                        taskCpltNum++;

                        robot.setSubPlanPointList(singlePathPlanning.getPlanPointList());
                        mapData.writePointsToMaps(robot.getSubPlanPointList(),robot.getTimeFree());
                    }
                }
            }
            for (Robot robot: conflictRobots) {
                mapData.clearPointsFromMaps(robot.getSubPlanPointList(),robot.getTimeFree());
            }

            if((totalCost<=totalCostMin) & (taskCpltNum>=taskCpltNumMax)){
                totalCostMin   = totalCost;
                taskCpltNumMax = taskCpltNum;
                for (Robot robot: conflictRobots) {
                    robot.setMainPlanPointList(robot.getSubPlanPointList());
                }
            }

        }
        for (Robot robot : conflictRobots) {
            mapData.writePointsToMaps(robot.getMainPlanPointList(),robot.getTimeFree());
            robot.setSubPlanPointList(new ArrayList<>());
            robot.assignTaskByPlan();
        }
    }

    private void optimizePlan(){
        System.out.println("optimizePlan");

        List<Robot> robotList = robotManager.getRobotList();
        Robot       lastRobot = robotList.get(robotList.size()-1);
        boolean     loop      = true;

        while (loop){
            for (Robot robot: robotList) {
                System.out.println("RobotId = " + robot.getId());
                Point lastPoint = robot.getLastPointByPlan();
                int   lastX     = lastPoint.getX();
                int   lastY     = lastPoint.getY();

                Robot fRobot    = null;
                for (int time = robot.getTimeFreeByPlan(); time < Context.timeMax; time++) {
                    PointInfo pointInfo = mapData.getMapByTime(time).getPointInfoByXY(lastX,lastY);
                    if(!pointInfo.isEmpty()){
                        fRobot = pointInfo.getRobot();
                        break;
                    }
                }

                if(fRobot != null){
                    System.out.println("fRobotId=" + fRobot.getId());
                    mapData.clearPointsFromMaps(fRobot.getMainPlanPointList(),fRobot.getTimeFree());
                    fRobot.setMainPlanPointList(new ArrayList<>());
                    fRobot.unassignTask();
                    break;
                }

                if (robot == lastRobot){
                    loop = false;
                }
            }

        }

    }




    public void execute(){
        clearOldPlan();
        assignTasks();
        simplePlan();
        priorPlan();

        optimizePlan();

        for (Robot robot: robotManager.getRobotList()) {
            robot.assignMainPlanPointList();
        }



    }

    public static class Config{
        public static int timeSolveMax = 1;


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

        private void add(Robot robot){
            if(!exist(robot))
                robotList.add(robot);
        }
        private boolean exist(Robot robot){
            for (Robot other: robotList) {
                if(robot == other)
                    return true;
            }
            return false;
        }
    }
}
